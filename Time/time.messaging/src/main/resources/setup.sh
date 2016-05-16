#!/usr/bin/env bash

wget -P /tmp https://www.rabbitmq.com/releases/rabbitmq-server/v3.6.1/rabbitmq-server_3.6.1-1_all.deb
sudo dpkg -i /tmp/rabbitmq-server_3.6.1-1_all.deb
sudo rabbitmq-plugins enable rabbitmq_management

# wget http://localhost:15672/ guest/guest