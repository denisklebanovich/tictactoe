module "vpc" {
  source = "./modules/vpc"
}

resource "aws_key_pair" "app_key" {
  key_name   = "app_key"
  public_key = file("~/.ssh/id_rsa.pub")
}

module "ec2" {
  source                = "./modules/ec2"
  count = 0
  key_name              = aws_key_pair.app_key.key_name
  app_security_group_id = module.vpc.app_security_group_id
  ami                   = var.ami
}

module "elastic_beanstalk" {
  source                = "./modules/elastic_beanstalk"
  count = 1
  app_name              = var.app_name
  key_name              = aws_key_pair.app_key.key_name
  app_vpc_id            = module.vpc.app_vpc_id
  app_security_group_id = module.vpc.app_security_group_id
  app_subnet_id         = module.vpc.app_subnet_id
}

module "fargate" {
  source                = "./modules/fargate"
  count = 1
  app_name              = var.app_name
  app_vpc_id            = module.vpc.app_vpc_id
  app_security_group_id = module.vpc.app_security_group_id
  app_subnet_id         = module.vpc.app_subnet_id
}