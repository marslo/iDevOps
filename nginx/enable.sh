#!/bin/bash
# =============================================================================
#    FileName: enable.sh
#      Author: marslo.jiao@gmail.com
#     Created: 2018-09-12 16:23:22
#  LastChange: 2018-09-12 16:24:40
#    Inspired: https://www.digitalocean.com/community/tutorials/how-to-install-nginx-on-ubuntu-16-04
# =============================================================================

sudo apt install nginx
sudo ufw allow 'Nginx Full'
sudo ufw allow 'Nginx HTTP'
sudo ufw allow 'Nginx HTTPS'
sudo ufw allow 'OpenSSH'

sudo ufw status

sudo systemctl enable nginx
sudo update-rc.d -f nginx defaults

sudo service --status-all
