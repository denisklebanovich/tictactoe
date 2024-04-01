variable "region" {
  description = "The region in which the resources will be deployed"
  default     = "us-east-1"
}

variable "ami" {
  description = "The AMI to use for the EC2 instance"
  default     = "ami-0c101f26f147fa7fd"
}