resource "aws_instance" "app_instance" {
  ami           = var.ami
  instance_type = "t2.micro"
  tags = {
    Name = "tictactoe"
  }
  key_name                    = var.key_name
  vpc_security_group_ids = [var.app_security_group_id]
  user_data = file("./setup.sh")
  associate_public_ip_address = true
  subnet_id                   = var.app_subnets[0]
}