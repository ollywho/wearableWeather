	<head>
	    <meta charset="utf-8">
	    <title>${title}</title>
	    <meta name="viewport" content="width=device-width, initial-scale=1">
		<script src="js/jquery-2.1.1.js"></script>
	    <link rel="stylesheet" type="text/css" href="css/bootstrap.min.css">
	    <link rel="stylesheet" type="text/css" href="css/ww_wardrobe_style.css">
	    <link rel="stylesheet" type="text/css" href="css/ww_home_style.css">
	    <script src="js/bootstrap.min.js"></script>
  	</head>
	<body>
		<nav id="navbar" class="navbar navbar-default">
		  <div class="container-fluid">
			<div class="navbar-header">
			  <a class="navbar-brand" href="home">Wearable Weather</a>
			</div>
			<ul id="nav" class="nav navbar-nav col-md-4 col-sm-4 pull-right">
			  <li class="nav-item"><a href="home">Forecast</a></li>
			  <li class="active nav-item"><a href="wardrobe">My Wardrobe</a></li>
			  <li id="logout"><a href="login">Log Out</a></li> 
			</ul>
		  </div>
		</nav>
		<div class="container">
			<!--<div class="row">-->
			<div id="undo" class="noshow">
				<p id="revert">Undo</p><p id="time">...5</p>
			</div>
			<div id="wardrobe-section" class="col-md-12">
				<div id="wardrobe-wrap" class="col-md-12">
					<div id="addGarmentBtn" data-toggle="modal" data-target="#myModal" type="button">
						<strong class="noselect">+</strong>
					</div>
					<div id="wardrobe-right" class="col-md-12">
					</div>
				</div>
			</div>

			<!-- Modal -->
			<div id="myModal" class="modal fade" role="dialog">
			  <div class="modal-dialog">
				<form>
					<!-- Modal content-->
					<div class="modal-content">
					  <div class="modal-header">
						<button style="color:white" type="button" class="close" data-dismiss="modal">&times;</button>
						<h4 class="modal-title">Add a Clothing Item</h4>
					  </div>
					  <div class="modal-body">
						<div class="form-group">
							<label for="color">Color:</label>
							<div id="color-wrap" class="col-md-12">
								<select class="form-control" id="color">
									<!--<option disabled selected value> -- select an option -- </option>
									<option style="background-color:blue;color:white">Blue</option>
									<option style="background-color:green;color:white">Green</option>
									<option style="background-color:red;color:white">Red</option>
									<option style="background-color:black;color:white">Black</option>-->
								</select>
								<div id="color-dropdown" class="col-md-12 noshow">
									<div class="row color-row">
										<div id="red" class="col-md-2 color-block">
										</div>
										<div id="blue" class="col-md-2 color-block">
										</div>
										<div id="orange" class="col-md-2 color-block">
										</div>
										<div id="yellow" class="col-md-2 color-block">
										</div>
										<div id="brown" class="col-md-2 color-block">
										</div>
										<div id="tan" class="col-md-2 color-block">
										</div>
									</div>
									<div class="row color-row">
										<div id="navy" class="col-md-2 color-block">
										</div>
										<div id="white" class="col-md-2 color-block">
										</div>
										<div id="black" class="col-md-2 color-block">
										</div>
										<div id="gray" class="col-md-2 color-block">
										</div>
										<div id="purple" class="col-md-2 color-block">
										</div>
										<div id="burgundy" class="col-md-2 color-block">
										</div>
									</div>
									<div class="row color-row">
										<div id="green" class="col-md-2 color-block">
										</div>
										<div id="pink" class="col-md-2 color-block">
										</div>
									</div>
								</div>
							</div>
						</div>
						<div class="form-group">
							<label style="margin-top:15px;" for="name">Name:</label>
							<input type="text" class="form-control" id="name"></input>
						</div>
						<div class="form-group">
							<label for="formality">Formality:</label>
							<div class="col-md-12">
								<div class="col-md-3">
									Very Casual
								</div>
								<div class="col-md-6">
									<input type="range" id="myRange" step="25" value="50">
								</div>
								<div class="col-md-3">
									Very Formal
								</div>
							</div>
						</div>
						<div class="form-group">
							<label for="clothing-type">Clothing Type:</label>
							<select class="form-control variable" id="clothing-type">
								<option disabled selected value> -- select an option -- </option>
								<option>Top</option>
								<option>Bottom</option>
							</select>
						</div>
						<div id="form-tops" class="noshow var-parent">
							<div class="form-group">
								<label for="top-layer">Clothing Type:</label>
								<select class="form-control variable layer" id="top-layer">
									<option disabled selected value> -- select an option -- </option>
									<option>Base</option>
									<option>Mid</option>
									<option>Outer</option>
								</select>
							</div>
							<div id="form-base-layer" class="form-group var-parent noshow">
								<label for="base-type">Type:</label>
								<select class="form-control variable weight" id="base-type">
									<option disabled selected value> -- select an option -- </option>
									<option>Short</option>
									<option>Long</option>
								</select>
							</div>
							<div id="form-outer-layer" class="form-group var-parent noshow">
								<label for="outer-type">Type:</label>
								<select class="form-control variable weight" id="outer-type">
									<option disabled selected value> -- select an option -- </option>
									<option>Light</option>
									<option>Heavy</option>
								</select>
								<label for="outer-waterproof">Waterproof:</label>
								<select class="form-control variable waterproof" id="outer-waterproof">
									<option disabled selected value> -- select an option -- </option>
									<option>Yes</option>
									<option>No</option>
								</select>
							</div>
						</div>
						<div id="form-bottoms" class="var-parent noshow">
							<div class="form-group">
								<label for="bottom-type">Type:</label>
								<select class="form-control variable weight" id="bottom-type">
									<option disabled selected value> -- select an option -- </option>
									<option>Short</option>
									<option>Long</option>
								</select>
							</div>
						</div>
					  </div>
					  <div class="modal-footer">
						<button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
						<button id="add-btn" type="submit" class="btn btn-default">Add</button>
					  </div>
					</div>
				</form>
			  </div>
			</div>
		</div>
		<script src="js/wardrobe.js"></script> 
	</body>