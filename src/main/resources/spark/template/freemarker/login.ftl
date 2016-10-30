	<head>
	    <meta charset="utf-8">
	    <title>${title}</title>
	    <meta name="viewport" content="width=device-width, initial-scale=1">
		<script src="js/jquery-2.1.1.js"></script>
	    <link rel="stylesheet" type="text/css" href="css/bootstrap.min.css">
	    <link rel="stylesheet" type="text/css" href="css/ww_login_style.css">
	    <script src="js/bootstrap.min.js"></script>
	    <script src="https://maps.googleapis.com/maps/api/js?key=AIzaSyC3dLIxBIzryJeI2PX9Q_J9YtpBbQVCrMs&libraries=places"></script>
	</head>
	<body>
		<div class="vertical-center">
			<div id="page-content" class="container">
				<div class="row">
					<div id="login-section" class="col-md-offset-4 col-md-4 center-text">
						<h1>
							Wearable Weather
						</h1>
						<div id="login-div" class="center-block">
							<div id="login-header" class="center-text">
								<p id="login-header-text">LOGIN TO YOUR ACCOUNT</p>
							</div>
							<form id="login-form" class="center-block">
								<div class="form-group">
									<input id="username" class="form-control form-input" placeholder="Username">
								</div>
								<div class="form-group">
									<input id="password" type="password" class="form-control form-input" placeholder="Password">
								</div>
									<button id="submit-btn" type="button" class="submit-btn btn">
									Login
									</button>
									<button id="signup-btn" type="button" class="submit-btn btn">
									Sign Up
									</button>
							</form>
						</div>
					</div>
				</div>
			</div>
		</div>
	    <script src="js/login.js"></script> 
	</body>