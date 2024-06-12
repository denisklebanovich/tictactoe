resource "aws_launch_template" "app_template" {
  name          = "app-template"
  image_id      = var.ami
  instance_type = "t2.micro"
  key_name      = var.key_name

  network_interfaces {
    security_groups             = [var.app_security_group_id]
    associate_public_ip_address = true
  }

  tag_specifications {
    resource_type = "instance"
    tags = {
      Name = "tictactoe"
    }
  }

  user_data = filebase64("./setup.sh")
}

resource "aws_autoscaling_group" "app_asg" {
  name     = "app-asg"
  max_size = 3
  desired_capacity = 1
  min_size = 0
  launch_template {
    id      = aws_launch_template.app_template.id
    version = "$Latest"
  }
  vpc_zone_identifier = var.app_subnets

  tag {
    key                 = "Name"
    value               = "tictactoe"
    propagate_at_launch = true
  }

  enabled_metrics = ["GroupInServiceInstances"]
}

resource "aws_autoscaling_policy" "scale_up" {
  name                   = "scale-up"
  scaling_adjustment     = 1
  adjustment_type        = "ChangeInCapacity"
  cooldown               = 300
  autoscaling_group_name = aws_autoscaling_group.app_asg.name
}

resource "aws_autoscaling_policy" "scale_down" {
  name                   = "scale-down"
  scaling_adjustment     = -1
  adjustment_type        = "ChangeInCapacity"
  cooldown               = 300
  autoscaling_group_name = aws_autoscaling_group.app_asg.name
}
