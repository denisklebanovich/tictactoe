resource "aws_launch_template" "app_template" {
  name                   = "app-template"
  image_id               = var.ami
  instance_type          = "t2.micro"
  key_name               = var.key_name
  vpc_security_group_ids = [var.app_security_group_id]
  network_interfaces {
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
  desired_capacity = 1
  max_size         = 3
  min_size         = 1
  launch_template {
    id      = aws_launch_template.app_template.id
    version = "$Latest"
  }

  vpc_zone_identifier = [var.app_subnet_id]

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
