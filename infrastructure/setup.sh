#!/bin/bash
# Install Docker and Docker Compose
echo "Installing Docker and Docker Compose"
sudo yum update -y
sudo yum install docker -y
sudo service docker start
sudo curl -L "https://github.com/docker/compose/releases/latest/download/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose
sudo chmod +x /usr/local/bin/docker-compose

# Install Git
echo "Installing Git"
sudo yum install git -y

# Clone the repository
echo "Cloning the repository"
git clone https://github.com/denisklebanovich/tictactoe.git
cd tictactoe || exit
echo "Starting the application"
sudo docker-compose up -d
