terraform {
  required_providers {
    aws = {
      source  = "hashicorp/aws"
      version = "~> 5.42"
    }
  }
}

provider "aws" {
  region = var.region
  default_tags {
    tags = {
      Name = "tic-tac-toe"
    }
  }
}