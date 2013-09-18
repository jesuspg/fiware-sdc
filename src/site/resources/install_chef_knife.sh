#!/bin/bash
# **************************************************************
# Name:      install_chef_client.s
# Purpose:   Install chef client and register with the che
#            server
# Notes:     This script must be run as root
#            This script installs the chef repo in the /root home
#            directory
#            Assumes these files exist in the same directory:
#            <-c input>.pem
#            <-o input>-validator.pem
#            knife.rb
# Credits:   Thanks to the groovy folks at Feedmagnet.com 
# Change Log
# Date           Name           Description
# ****************************************************************
# 2011/02/22     BEH            File Creation
# 2011/02/28     BEH            Put chef repo in root directory
# ****************************************************************
# Includes
#. lib/global.sh
#. lib/got_root.sh






# ****************************************************************
# Script specific configuration
# ****************************************************************
# Path for Ruby Gems install
RUBY_GEM_SOURCE="http://production.cf.rubygems.org/rubygems/rubygems-1.3.7.tgz"


# Define the script's usage from the command line.
USAGE="
    This script installs the Chef Client on to a new slice and registers it 
    with the OpsCode Platform (Chef Server).    
    
    This script expects to be root.
    
    Usage:  $SCRIPT_NAME [-c] [-o] -h
            -h show this message
            -c user name for the chef account at opscode
            -o organization name of the chef accoutn at opscode
    
    Example(s):
        $SCRIPT_NAME -u got_root -p my_secret -c chef_user -o my_corp
        
       
"




# ****************************************************************
# Process options
# ****************************************************************
while getopts "hc:o:" OPTION
do
    case "$OPTION" in
        h ) echo "$USAGE";exit 0;;
        c ) OPSCODE_USER=${OPTARG};;
        o ) OPSCODE_ORGANIZATION=${OPTARG};;
        [?] ) echo "    ${OPTIND} is unimplemented $USAGE";;
    esac
done  




# ****************************************************************
# Define functions.
# ****************************************************************
# Install Ruby and Development Tools
function install_ruby
{
    MSG="++++ Install Ruby and Development Tools"
    echo $MSG
    echo "    sudo apt-get install ruby ruby-dev libopenssl-ruby rdoc ri irb build-essential wget ssl-cert git-core"
    apt-get --assume-yes install ruby ruby-dev libopenssl-ruby rdoc ri irb build-essential wget ssl-cert git-core
}


# Install RubyGems and the Chef gem
function install_gems
{
    MSG="++++ Install RubyGems and the Chef gem"
    echo $MSG
    echo "    wget -O /tmp/ruby_gems.tgz \"$RUBY_GEM_SOURCE\""
    wget -O /tmp/ruby_gems.tgz "$RUBY_GEM_SOURCE"
    echo "    tar -xzf /tmp/ruby_gems.tgz"
    tar -xzf /tmp/ruby_gems.tgz    
    echo "    /tmp/rubygems-1.3.7/setup.rb --no-format-executable"
    ruby /tmp/rubygems-1.3.7/setup.rb --no-format-executable   
    echo "    gem install chef"
    gem install chef 
}


# Create a Chef Repository 
function make_chef_repo
{
    MSG="++++ Create a Chef Repository"
    echo $MSG
    echo "    git clone git://github.com/opscode/chef-repo.git /root/chef-repo"
    git clone git://github.com/opscode/chef-repo.git /root/chef-repo
}


# Create .chef Directory 
function make_chef_dir
{
    MSG="++++ Create .chef Directory"
    echo $MSG
    echo "    mkdir -p /root/chef-repo/.chef"
    mkdir -p /root/chef-repo/.chef
    echo "    mv /tmp/$OPSCODE_USER.pem /root/chef-repo/.chef"
    mv /tmp/$OPSCODE_USER.pem /root/chef-repo/.chef
    echo "    mv /tmp/$OPSCODE_ORGANIZATION-validator.pem /root/chef-repo/.chef"
    mv /tmp/$OPSCODE_ORGANIZATION-validator.pem /root/chef-repo/.chef
    echo "    mv /tmp/knife.rb /root/chef-repo/.chef"
    mv /tmp/knife.rb /root/chef-repo/.chef
}


# Generate Chef Client Configuration
function test_connection_chef-server
{
    MSG="++++ Test connection to Chef Server"
    echo $MSG
    echo "    cd /root/chef-repo"
    cd /root/chef-repo
    echo "    knife client list"
    knife client list
}

# Generate Chef Client Configuration
#function config_chef
#{
#    MSG="++++ Generate Chef Client Configuration"
#    echo $MSG
#    echo "    cd /root/chef-repo"
#    cd /root/chef-repo
#    echo "    knife configure client ./client-config"
#    knife configure client ./client-config
#    echo "    cp -r /root/chef-repo/client-config /etc/chef"
#    cp -r /root/chef-repo/client-config /etc/chef
#    echo "    chef-client"
#    chef-client
#}






function main 
{
    got_root                    # from got_root.sh
    install_ruby
    install_gems
    make_chef_repo
    make_chef_dir
    test_connection_chef-server
}


time main
