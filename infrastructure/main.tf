module "vpc" {
  source = "./modules/vpc"
}

resource "aws_key_pair" "app_key" {
  key_name = "app_key"
  public_key = file("~/.ssh/id_rsa.pub")
}

module "ec2" {
  source                = "./modules/ec2"
  ami                   = var.ami
  app_security_group_id = module.vpc.app_security_group_id
  app_subnets           = module.vpc.app_subnets
  key_name              = aws_key_pair.app_key.key_name
}


module "cognito" {
  source = "./modules/cognito"
}

module "storage" {
  source            = "./modules/storage"
  vpc_id            = module.vpc.app_vpc_id
  security_group_id = module.vpc.app_security_group_id
  vpc_subnets       = module.vpc.app_subnets
}