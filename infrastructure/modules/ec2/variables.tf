variable "ami" {
  type        = string
  description = "The AMI to use for the instance"
}

variable "key_name" {
  type        = string
  description = "The key pair name to use for the instance"
}

variable "app_security_group_id" {
  type        = string
  description = "The security group ID for the application"
}

variable "app_subnet_id" {
  type        = string
  description = "The subnet ID for the application"
}

variable "alert_email" {
  type        = string
  description = "The email address to send alerts to"
}