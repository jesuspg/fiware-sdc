#!/usr/bin/python2.6 -tt
# -*- coding: utf-8 -*-

import sys, commands, os, subprocess

## CONSTANTS ##
_usage = "Usage: newVM.py <os_type> <vm_name>"

_distros = {"debian5" : "Instalación básica de Debian Lenny (5.0)" ,
	        "centos55" : "Instlación básica de Centos 5.5"}

_tmpl = {"debian5" : "debian-base",
		 "centos55" : "centos-base"}

_root_dir = "/home/images/kvm"
_libvirt_dir = "/etc/libvirt/qemu/"

_clone = "virt-clone --original %s --name %s --file %s/vdisk.img"
_mount = "mount -o loop,offset=32256 %s/vdisk.img %s/temp"
_unmount = "umount %s/temp"

_sed_name = "sed -e s/%s/%s/g -i %s/temp/%s"
_sed_mac = "sed -e s/%s/%s/g -i %s/temp/%s"

_cat_mac = 'cat %s/%s.xml | grep "mac address" | cut -d"\'" -f2'

_virsh_define = "virsh define %s/%s.xml"
_virsh_start = "virsh start %s"

_replace_name_files = {"debian5"  : ["/etc/hosts",
									 "/etc/hostname",
									 "/etc/dhcp3/dhclient.conf",
									 "/etc/network/interfaces"],
					   "centos55" : ["/etc/hosts",
									 "/etc/sysconfig/network",
									 #"/etc/sysconfig/networking/profiles/default/hosts", 
									 #"/etc/sysconfig/networking/profiles/default/ifcfg-eth0", #]}
									 "/etc/sysconfig/network-scripts/ifcfg-eth0"]}

_replace_mac_files = {"debian5"  : ["/etc/udev/rules.d/70-persistent-net.rules"],
					  "centos55" : ["/etc/sysconfig/networking/profiles/default/ifcfg-eth0", #]}
									"/etc/sysconfig/network-scripts/ifcfg-eth0"]}

###############

def usage():
	print _usage #%("|".join(_distros.keys()))
	list()
	print 

def options():
	print "Opciones disponibles:"
	for opt, desc in _options.items():
		print "  "+opt+":\t"+desc


def list():
	print "  Lista de sistemas disponibles:"
	for os, desc in _distros.items():
		print "    "+os+":\t"+desc

def run(cmd):
	p = subprocess.Popen(cmd, shell=True, stdout=subprocess.PIPE, close_fds=True)
	return p.wait()

def run_stat(cmd):
	return commands.getoutput(cmd)

def mount_lvm(dir):
	offset=106928640
	loop_dev = run_stat("losetup -f")
	#print loop_dev
	run("losetup %s -o%s %s/vdisk.img" %(loop_dev, offset, dir))

	vol = "/dev/"+run_stat("pvscan | grep %s | awk '{print $4}'" %(loop_dev))+"/LogVol00"
	#print vol

	run("lvchange -a y "+vol)
	run("mount %s %s" %(vol, dir+"/temp"))

	return loop_dev

def umount_lvm(dir, loop_dev):
	run("umount %s/temp" %(dir))
	vol = run_stat("pvscan | grep %s | awk '{print $4}'" %(loop_dev))
	#print vol
	run("lvchange -a n %s" %(vol))
	run("losetup -d %s" %(loop_dev))

def check_name(newvm):
	new_dir = os.path.join(_root_dir, newvm)
	if os.path.isdir(new_dir):
		return

	existsing_vm = commands.getoutput('virsh list --all').split("\n")
	for vm_line in existsing_vm[2:-1]:
		if newvm == vm_line.split()[1]:
			return

	return 1

def create(new_vm, distro):
	print "Creating new VM "+new_vm
	new_dir = os.path.join(_root_dir, new_vm)
	base_name = _tmpl[distro]
	os.makedirs(os.path.join(new_dir,"temp"))
	
	run(_clone %(base_name, new_vm, new_dir))

	print "Configuring "+new_vm

	loop_dev = ""
	if base_name == "debian-base":
		run(_mount %(new_dir, new_dir))
	elif base_name == "centos-base":
		loop_dev = mount_lvm(new_dir)
	else:
		print "imposible"
		sys.exit(666)

	for file in _replace_name_files[distro]:
		run(_sed_name %(base_name, new_vm, new_dir, file))

	base_mac = run_stat(_cat_mac %(_root_dir+"/"+base_name, base_name))
	new_mac = run_stat(_cat_mac %(_libvirt_dir, new_vm))

	if base_name == "centos-base":
		base_mac = base_mac.upper()
		new_mac = new_mac.upper()

	#print base_mac 
	#print new_mac

	for file in _replace_mac_files[distro]:
		run(_sed_mac %(base_mac, new_mac, new_dir, file))
	
	if base_name == "debian-base":
		run(_unmount %(new_dir))
	elif base_name == "centos-base":
		umount_lvm(new_dir, loop_dev)
	else:
		print "imposible"
		sys.exit(666)

	print "Starting virtual machine"
	run(_virsh_define %(_libvirt_dir, new_vm))
	run(_virsh_start %(new_vm))
	print "Wait until boot is finished and then execute:"
	print "ssh -l root "+new_vm
	print

def main(argc, argv):
	if argc == 0:
		usage()

	elif argc == 1:
		opt = argv[0]
		if opt == "--list":
			list()
		elif opt == "--help":
			usage()
		else:
			usage()

	elif argc == 2:
		os_type = argv[0]
		if not os_type in _distros:
			print "ERROR: La distro solicitada no está disponible"
			usage()
			return 1000
		
		vm_name = argv[1]
		if not check_name(vm_name):
			print "ERROR: El nombre "+vm_name+" ya está siendo usado"
			usage()
			return 1001
	
		create(vm_name, os_type)
	
	else:
		print "ERROR: Too many arguments"
		usage()
		
#

if __name__ == "__main__":
   	sys.exit(main(len(sys.argv[1:]), sys.argv[1:]))

