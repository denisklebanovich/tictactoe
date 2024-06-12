resource "aws_s3_bucket" "profile_pictures" {
  bucket = "tic-tac-toe-profile-pictures-denisklebanovich"
  tags = {
    Name = "Tic Tac Toe Profile Pictures"
  }
}

resource "aws_s3_object" "user1_profile_picture" {
  bucket = aws_s3_bucket.profile_pictures.bucket
  key    = "user1.png"
  source = "./avatars/avatar_1.png"
}

resource "aws_s3_object" "user2_profile_picture" {
  bucket = aws_s3_bucket.profile_pictures.bucket
  key    = "user2.png"
  source = "./avatars/avatar_2.png"
}

resource "aws_db_subnet_group" "tictactoe_db_subnet_group" {
  name       = "tictactoe-db-subnet-group"
  subnet_ids = var.vpc_subnets
}

resource "aws_db_instance" "tictactoe_db" {
  allocated_storage    = 20
  engine               = "postgres"
  publicly_accessible  = true
  engine_version       = "16.3"
  instance_class       = "db.t3.micro"
  username             = "tictactoe_user"
  password             = "strongpassword123"
  skip_final_snapshot  = true
  db_name              = "tictactoedb"
  vpc_security_group_ids = [aws_security_group.allow_backend_access.id]
  db_subnet_group_name = aws_db_subnet_group.tictactoe_db_subnet_group.name
  availability_zone    = "us-east-1a"
}


resource "aws_security_group" "allow_backend_access" {
  vpc_id = var.vpc_id

  ingress {
    from_port = 5432
    to_port   = 5432
    protocol  = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }

  egress {
    from_port = 0
    to_port   = 0
    protocol  = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }
}
