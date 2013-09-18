#!/usr/bin/python2.6 -tt
# -*- coding: utf-8 -*-

import sys, commands, os, subprocess, time

_root_dir = "/home/images/kvm/"
_repo_dir = "/var/www/images/"

#
def usage():
	print "Usage: haltVM.py <vm_name>"

#
def run(cmd):
    p = subprocess.Popen(cmd, shell=True, stdout=subprocess.PIPE, close_fds=True)
    return p.wait()

#
def halt(vm_name):
    print "Shuting down "+vm_name+" ... ",
    run('ssh -o StrictHostKeyChecking=no -l root '+vm_name+' halt')
    time.sleep(10)
        
    t = 1.0
    acc = 0.5
    while True:
        status = commands.getoutput("virsh list --all | grep "+vm_name).split()
        #print status
        if "off" in status:
            break
        else:
            print "durmiendo por "+str(t)+" segundos"
            time.sleep(t)
            t+=2+acc*t
            
        if t > 10.0:
            run('virsh destroy '+vm_name)
    print "done"

#
def move(vm_name):
    print "Copiando la imagen al repositorio ... ",
    #run("mv "+_root_dir+"/"+vm_name+"/vdisk.img "+_repo_dir+"/"+vm_name+".img")
    run("tar -Sczf "+_repo_dir+"/"+vm_name+".img.tgz "+_root_dir+"/"+vm_name+"/vdisk.img")
    print "done"
    print "Destruyendo los restos ...",
    run("virsh undefine "+vm_name)
    run("rm -rf "+_root_dir+"/"+vm_name)
    print "done"

#
def main(argc, argv):
    if argc != 1:
        usage()
        return 1

    vm_name = argv[0]
    halt(vm_name)
    move(vm_name)

if __name__ == "__main__":
	sys.exit(main(len(sys.argv[1:]), sys.argv[1:]))

