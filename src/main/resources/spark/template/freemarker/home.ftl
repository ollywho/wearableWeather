<!DOCTYPE html>
  <head>
    <meta charset="utf-8">
    <title>${title}</title>
    <meta name="viewport" content="width=device-width, initial-scale=1">
	<script src="js/jquery-2.1.1.js"></script>
    <link rel="stylesheet" type="text/css" href="css/bootstrap.min.css">
    <link rel="stylesheet" type="text/css" href="css/ww_home_style.css">
    <script src="js/api.js"></script> 
    <script src="js/bootstrap.min.js"></script>
    <script src="https://maps.googleapis.com/maps/api/js?key=AIzaSyC3dLIxBIzryJeI2PX9Q_J9YtpBbQVCrMs&libraries=places"></script>
  </head>
  <body>
    
      
    <nav id="navbar" class="navbar navbar-default">
		  <div class="container-fluid">
			<div class="navbar-header">
			  <a class="navbar-brand" href="home">Wearable Weather</a>
			</div>
			<ul id="nav" class="nav navbar-nav col-md-4 col-sm-4 pull-right">
			  <li class="active nav-item"><a href="home">Forecast</a></li>
			  <li class="nav-item"><a href="wardrobe">My Wardrobe</a></li>
			  <li id="logout"><a href="login">Log Out</a></li>  
			</ul>
		  </div>
		</nav>
		<div class="container">
			<!--<div class="row">-->
				<div class="col-md-6 col-sm-6">
					<div class="row">
						<div style="text-align:center;margin-bottom:0px;" class="form-group col-md-4 col-md-offset-1">
							<h4 id="curr-loc" class="city-txt"></h4>
							<!--<input id="city-input" class="form-control"></input>-->
						</div>
						<div class="col-md-5">
							<p id="new-city" class="city-txt">select a different city</p>
							<input id="city-input" class="form-control noshow"></input>
						</div> 
					</div>
					<div class="row">
					<div class="col-md-6 col-sm-6 center-align-text">
						<div id="today-temp-div">
						68&deg;
						</div>
					</div>
					<div class="col-md-6 col-sm-6">
						<div class="day-img-div margin-10">
							<img id="today-img" height="85" width="85"/>
						</div>
					</div>
					</div>
					<div class="row">
						<div id="today-outfit-txt" class="col-md-6">
						Today's Outfit
						</div>
						<div id="today-accessory-txt" class="col-md-6">
							<div id="accessories-txt">
							Accessories
							</div>
						</div>
					</div>
					<div id="outfit-wrap" class="noshow">
						<div class="row">
							<div class="col-md-6">
								<div class="row">
									<div id="top-img" class="clothing-img">
										<!--<img id="top-img" data-toggle="popover" data-placement="left" data-trigger="hover" data-content="" class="clothing-img" height="120" width="100"/>-->
										<canvas id="topCanvas" height="120" width="100" data-toggle="popover" data-placement="left" data-trigger="hover" data-content="">
										</canvas>
									</div>
								</div>
								<div class="row">
									<div id="bottom-img" class="clothing-img">
										<!--<img data-toggle="popover" data-placement="left" data-trigger="hover" data-content="" id="bottom-img" class="clothing-img"/>-->
										<canvas id="btmCanvas" height="120" width="100" data-toggle="popover" data-placement="left" data-trigger="hover" data-content="">
										</canvas>
									</div>
								</div>
								<div class="row">
									<div id="refresh-btn-div">
										<div class="inline-block">
											<img src="img/refresh_icon.png" height="25" width="25"/>
										</div>
										<div id="suggestion-txt" class="inline-block">
										New Suggestion
										</div>
									</div>
								</div>
							</div>
							<div class="col-md-6">
								<div class="margin-10">
									<img id="accessory-img"/>
								</div>
							</div>
						</div>
					</div>
					<div id="formality-section" class="row">
						<div id="formality-input" class="col-md-6">
							<input type="checkbox" id="specific-formality" name="specific-formality" value="specific-formality"> Select Specific Formality
							<div id="formality-slider" class="noshow">
								<div class="col-md-3">
											Very Casual
								</div>
								<div class="col-md-6">
									<input type="range" id="formalityRange" step="25" value="50">
								</div>
								<div class="col-md-3">
									Very Formal
								</div>
							</div>
							<button id="get-sugg-btn" class"btn">
								Get Outfit Suggestion
							</button>
						</div>
						<div class="col-md-6">
						</div>
					</div>
					<div id="suggest-clothes" class="row noshow">
						<div style="text-align:center;margin-top:15px;" class="col-md-6">
							<p>You do not have any clothes appropriate for this weather.</p>
							<a href="wardrobe">Add Clothes</a>
						</div>
						<div class="col-md-6">
						</div>
					</div>
				</div>
				<div class="col-md-6 col-sm-6">
					<div class="row">
					<div id="weather-details-section" class="col-md-12 col-sm-12">
						<div class="col-md-6 col-sm-6 no-padding">
							<ul class="pull-right left-list">
								<!--<li>High | Low:</li>-->
								<li>Feels Like:</li>
								<li>Precipitation:</li>
								<li>Wind:</li>
								<li>Humidity:</li>
								<li>UV Index:</li>
							</ul>
						</div>
						<div class="col-md-6 col-sm-6 no-padding">
							<ul class="pull-left right-list">
								<!--<li id="high-low">72&deg; | 60&deg;</li>-->
								<li id="feels">68&deg;</li>
								<li id="precip">0%</li>
								<li id="wind">0 | 3 MPH</li>
								<li id="humid">55%</li>
								<li id="uv">05 - Moderate Risk</li>
							</ul>
						</div>
					</div>
					</div>
					<div class="row">
					<div id="weather-forecast-section" class="col-md-12 col-sm-12">
						<div id="upcoming-div" class="row">
						Upcoming Weather
						</div>
						<div class="row">
						<div class="weather-tile">
							<div class="tile-header">
							</div>
							<div class="tile-body">
								<div class="day-temps">
									<div class="day-temp-f">68&deg;</div>
									<div class="day-temp-c">51&deg;</div>
								</div>
								<div class="day-img-div">
									<img class="day-img" height="55" width="55"/>
								</div>
							</div>
						</div>
						<div class="weather-tile">
							<div class="tile-header">
							</div>
							<div class="tile-body">
								<div class="day-temps">
									<div class="day-temp-f">69&deg;</div>
									<div class="day-temp-c">42&deg;</div>
								</div>
								<div class="day-img-div">
									<img class="day-img" height="55" width="55"/>
								</div>
							</div>
						</div>
						<div class="weather-tile">
							<div class="tile-header">
							</div>
							<div class="tile-body">
								<div class="day-temps">
									<div class="day-temp-f">64&deg;</div>
									<div class="day-temp-c">47&deg;</div>
								</div>
								<div class="day-img-div">
									<img class="day-img" height="55" width="55"/>
								</div>
							</div>
						</div>
						<div class="weather-tile">
							<div class="tile-header">
							</div>
							<div class="tile-body">
								<div class="day-temps">
									<div class="day-temp-f">74&deg;</div>
									<div class="day-temp-c">57&deg;</div>
								</div>
								<div class="day-img-div">
									<img class="day-img" height="55" width="55"/>
								</div>
							</div>
						</div>
						<div class="weather-tile">
							<div class="tile-header">
							</div>
							<div class="tile-body">
								<div class="day-temps">
									<div class="day-temp-f">72&deg;</div>
									<div class="day-temp-c">58&deg;</div>
								</div>
								<div class="day-img-div">
									<img class="day-img" height="55" width="55"/>
								</div>
							</div>
						</div>
						</div>
					</div>
					</div>
				</div>
			<!--</div>-->
		</div>
  </body>
  
  <!--<script src="js/api.js"></script>--> 
  <#include "main.ftl">
</html>