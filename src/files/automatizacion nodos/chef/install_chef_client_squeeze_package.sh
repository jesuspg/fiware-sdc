echo 'deb http://apt.opscode.com/ squeeze main' | tee /etc/apt/sources.list.d/opscode.list
wget -qO - http://apt.opscode.com/packages@opscode.com.gpg.key | apt-key add -
apt-get update
apt-get install chef -y
