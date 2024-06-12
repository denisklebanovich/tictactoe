resource "aws_cognito_user_pool" "user_pool" {
  name = "user-pool"
  alias_attributes = ["email"]
  auto_verified_attributes = ["email"]
  password_policy {
    minimum_length = 6
  }
}

resource "aws_cognito_user_pool_client" "user_pool_client" {
  name = "app-client"

  user_pool_id    = aws_cognito_user_pool.user_pool.id
  generate_secret = false
  explicit_auth_flows = ["ALLOW_REFRESH_TOKEN_AUTH", "ALLOW_USER_PASSWORD_AUTH", "ALLOW_ADMIN_USER_PASSWORD_AUTH"]
}

resource "aws_cognito_user" "user1" {
  username     = "user1"
  password     = "password"
  user_pool_id = aws_cognito_user_pool.user_pool.id
  attributes = {
    email          = "denis.klebanovich@gmail.com"
    email_verified = true
  }
}

resource "aws_cognito_user" "user2" {
  username     = "user2"
  password     = "password"
  user_pool_id = aws_cognito_user_pool.user_pool.id
  attributes = {
    email          = "269537@student.pwr.edu.pl"
    email_verified = true
  }
}