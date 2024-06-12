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

# Add the .env file
echo "Adding the .env file"
touch .env
echo "RDS_ENDPOINT=${rds_endpoint}" >> .env
echo "AWS_ACCESS_KEY_ID=${aws_access_key_id}" >> .env
echo "AWS_SECRET_ACCESS_KEY=${aws_secret_access_key}" >> .env
echo "AWS_SESSION_TOKEN=${aws_session_token}" >> .env

echo "Starting the application"
sudo docker-compose up -d
