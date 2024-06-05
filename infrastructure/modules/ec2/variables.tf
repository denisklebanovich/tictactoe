variable "ami" {
  type        = string
  description = "The AMI to use for the instance"
}

variable "key_name" {
  type        = string
  description = "The key pair name to use for the instance"
}

variable "vpc_id" {
  type        = string
  description = "The VPC ID"
}

variable "app_security_group_id" {
  type        = string
  description = "The security group ID for the application"
}

variable "app_subnets" {
  type        = list(string)
  description = "The subnets to deploy the application in"
}

variable "alert_email" {
  type        = string
  description = "The email address to send alerts to"
}