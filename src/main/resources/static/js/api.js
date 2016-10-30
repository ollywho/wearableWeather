var lat;
  var lng;
  var city;
  var state; 
  var currObs;
  var forecast;
  var currDay;
  var currWeekday;
  var currMonth;
  var currHigh;
  var currLow;
  var currPop;
  var RAIN = 0.3;
  var SUNNY = 6;
  var FREEZING = 40;
  var suggs;
  var numOutfits;
  var outfitIdx;
  var formality;
  var accessory_img_size;
  var forecastError = false;
  var days = {};
  days["Monday"] = "MON";
  days["Tuesday"] = "TUES";
  days["Wednesday"] = "WED";
  days["Thursday"] = "THURS";
  days["Friday"] = "FRI";
  days["Saturday"] = "SAT";
  days["Sunday"] = "SUN";
  var colors = {};
  colors["white"] = [255,255,255];
  colors["red"] = [255,0,0];
  colors["blue"] = [0,93,255];
  colors["navy"] = [0,0,110];
  colors["brown"] = [139,69,19];
  colors["green"] = [85,107,47];
  colors["orange"] = [255,165,0];
  colors["yellow"] = [255,255,0];
  colors["tan"] = [210,180,140];
  colors["black"] = [0,0,0];
  colors["gray"] = [128,128,128];
  colors["purple"] = [128,0,128];
  colors["burgundy"] = [139,0,0];
  colors["pink"] = [255,192,203];
  var auto_key = "AIzaSyC3dLIxBIzryJeI2PX9Q_J9YtpBbQVCrMs";
  
var place;
var input;
var newCity;
  function init() {
	  $.ajax({
	  url : "http://api.wunderground.com/api/42fc2d91ce5af419/geolookup/q/autoip.json",
	  dataType : "jsonp",
	  success : function(parsed_json) {
	  x = parsed_json;
	  city = parsed_json['location']['city'];
	  state = parsed_json['location']['state'];
    getCurr();
    getFore();
	  }
	  });
	 }
	 
function getCurr() {
  if (state != undefined && city != undefined){
    $.ajax({
    url : "http://api.wunderground.com/api/42fc2d91ce5af419/conditions/q/" + state + "/" + city + ".json",
    dataType : "jsonp",
    success : function(parsed_json) {
    	var x = parsed_json;
    	console.log(x);
    if(x.current_observation != null){	
	    var uv = Math.floor(parsed_json['current_observation']['UV']);
	    var feelsLike = parsed_json['current_observation']['feelslike_f'];
	    var iconDesc = parsed_json['current_observation']['icon'];
	    var iconUrl = parsed_json['current_observation']['icon_url'];
	    var precip = Math.floor(parsed_json['current_observation']['precip_today_in']);
	    var tempF = Math.floor(parsed_json['current_observation']['temp_f']);
	    var descrip = parsed_json['current_observation']['weather'];
	    var wind = parsed_json['current_observation']['wind_mph'];
	    var humid = parsed_json['current_observation']['relative_humidity'];
	    currObs = {uv: uv, feelsLike: feelsLike, iconDesc: iconDesc, iconUrl: iconUrl, precip: precip, tempF: tempF, descrip: descrip, wind: wind, humid: humid}
	    }
    }
    });
  } else {
    console.log("ERROR: must get location");
  }
  }
  
  function getFore() {
  $.ajax({
  url : "http://api.wunderground.com/api/42fc2d91ce5af419/forecast10day/q/" + state + "/" + city + ".json",
  dataType : "jsonp",
  success : function(parsed_json) {
  x = parsed_json;
  if(parsed_json.forecast){
	  var forecasts = parsed_json.forecast.simpleforecast.forecastday;
	  currDay = forecasts[0].date.day;
	  currWeekday = forecasts[0].date.weekday;
	  currMonth = forecasts[0].date.monthname;
	  currHigh = Math.floor(forecasts[0].high.fahrenheit);
	  currLow = Math.floor(forecasts[0].low.fahrenheit);
	  currPop = Math.floor(forecasts[0].pop);
	  forecast = {};
	  for(i = 1; i < 6; i++){
	    forecast[i-1] = new Object();
	    var currentday = forecast[i-1];
	    currentday['weekday'] = forecasts[i].date.weekday;
	    currentday['high'] = Math.floor(forecasts[i].high.fahrenheit);
	    currentday['low'] = Math.floor(forecasts[i].low.fahrenheit);
	    currentday['icon'] = forecasts[i].icon_url;
	  }
  }
  else{
	  forecastError = true;
  }
  }
  });
  }
  
  function isRaining() {
    if(currObs != undefined){
      return currObs.precip >= RAIN;
    } else {
      return null;
    }
  }
  function isSunny() {
    if(currObs != undefined){
      return currObs.uv >= SUNNY;
    } else {
      return null;
    }
  }
  function isFreezing() {
    if(currObs != undefined){
      return currObs.tempF <= FREEZING;
    } else {
      return null;
    }
  }
  
  function getSuggs() {
  var postParameters = {uv: currObs.uv, high: currObs.tempF, precip: currObs.precip, formality:formality};
  $.post("/suggestion", postParameters,  function(responseJSON){
    returnedObject = JSON.parse(responseJSON);
    suggs = returnedObject;
    outfitIdx = undefined;
    numOutfits = suggs.garb.length;
  });
  }
  
  function newSugg(){
    if(suggs != undefined){
      if(outfitIdx == undefined) {
        outfitIdx = 0;
        return suggs.garb[0];
      } else {
        outfitIdx = (outfitIdx + 1) % numOutfits;
        return suggs.garb[outfitIdx];
      }
    } else {
      return null;
    }
  }
  
  $(document).ready(function(evt){
	  input = (document.getElementById('city-input'));
	  newCity = document.getElementById('new-city');
	  var autocomplete = new google.maps.places.Autocomplete(input);
	  autocomplete.addListener('place_changed', function() {place = autocomplete.getPlace(); changelctn();});
	  function changelctn(){
		  if (place != undefined){
		    var state_found = false;
		    var city_found = false;
		    var new_state = "";
		    var new_city = "";
		    for (var i = 0; i < place.address_components.length; i++) {
		    var addressType = place.address_components[i].types[0];
		      if (addressType == "locality") {
		        new_city = place.address_components[i].long_name;
		        city_found = true;
		      } else if (addressType == "administrative_area_level_1") {
		        new_state = place.address_components[i].short_name;
		        state_found = true;
		      }
		    }
		    if (state_found && city_found) {
		      input.classList.add('noshow');
		      newCity.classList.remove('noshow');
		      $("#suggest-clothes").addClass("noshow");
		      state = new_state;
		      city = new_city.replace(/ /g,'_');
		      getCurr();
			  getFore();
			  if(forecastError){
				  forecastError = false;
				  input.value = "bad input";
			  }
			  else{
				  showFormality();
			  }
		    } else {
		      input.value = "bad input";
		    }
		  } else {
		    input.value = "Enter a location";
		  }
		}
	  init();
	  $("#new-city").click(function(){
		  $("#city-input").removeClass("noshow");
		  $(this).addClass("noshow");
	  });
	  $("#specific-formality").change(function(){
		  if($(this).is(":checked")){
			  $("#formality-slider").removeClass("noshow");
			  $("#suggest-clothes").addClass("noshow");
		  }
		  else{
			  $("#formality-slider").addClass("noshow");
		  }
		  
	  })
	  $("#get-sugg-btn").click(function(){
		  $('#specific-formality').attr('checked',false);
		 if($("#formality-slider").hasClass("noshow")){
			 formality = 0;
		 } 
		 else{
			 $("#formality-slider").addClass("noshow");
			 var formalVal;
			 var sliderVal = $("#formalityRange").val();
			 switch(sliderVal){
			 case "0":
				 formalVal = 1;
				 break;
			 case "25":
				 formalVal = 2;
				 break;
			 case "50":
				 formalVal = 3;
				 break;
			 case "75":
				 formalVal = 4;
				 break;
			 case "100":
				 formalVal = 5;
				 break;
			 default:
				 formalVal = 0;
			 }
			 formality = formalVal;
		 }
		 $("#formality-section").addClass("noshow");
		 $("#outfit-wrap").removeClass("noshow");
		 getSuggs();
	  });
	  $(".nav-item").click(function(evt){
		 var target = $(evt.target).parent();
		 if(!target.hasClass("active")){
			 $( ".active" ).each(function(){
				 $(this).removeClass("active");
			 });
			 target.addClass("active");
		 }
	  });
	  $("#refresh-btn-div").click(function(){
		  updateOutfit();
	  });
	  $("[data-toggle=popover]").popover();
	  function updateOutfit(){
		  var suggestion = newSugg();
		  clearCanvases();
		  var bottom_img_src = (suggestion[1].weight == "long")?"img/pants.png":"img/shorts.png";
		  var top_img_src = getTopSrc(suggestion);
		  var btmImg = new Image();
		  btmImg.src = bottom_img_src;
		  btmImg.onload = function(){
			  drawImg(this,colors[suggestion[1].color],"btmCanvas",bottom_img_src);
		  }
		  var topImg = new Image();
		  topImg.src = top_img_src;
		  topImg.onload = function(){
			  drawImg(this,colors[suggestion[0].color],"topCanvas",top_img_src);
		  }
		  $("#topCanvas").attr("data-content",suggestion[0].name);
		  $("#btmCanvas").attr("data-content",suggestion[1].name);
	  }
	  function showFormality(){
		  $("#formality-section").removeClass("noshow");
		  $("#outfit-wrap").addClass("noshow");
	  }
	  function getTopSrc(suggestion){
		  if(suggestion[0].layer=="base"){
			  if(suggestion[0].weight=="short"){
				  return "img/shirt.png";
			  }
			  else{
				  return "img/button-down.jpg";
				  //return "img/shirt.png";
			  }
		  }
		  else if(suggestion[0].layer=="mid"){
			  return "img/sweater.png";
		  }
		  else{
			  if(suggestion[0].weight="heavy"){
				//return "img/heavy-jacket.png";
				  return "img/light-jacket.png";
			  }
			  else{
				  return "img/light-jacket.png";
			  }
		  }
	  }
	  function drawImg(img,rgb,canvasId,source){
		  var canvas = document.getElementById(canvasId);
		  var ctx = canvas.getContext("2d");
		  ctx.drawImage(img,0,0,100,120);
		  var image = ctx.getImageData(0,0,canvas.width,canvas.height);
		  var data = image.data;
		  var color = function(){
			  for(var i = 0; i<data.length;i+=4){
				  if(data[i]<10 && data[i+1]<10 && data[i+2]<10){
					  data[i] = rgb[0];
					  data[i+1] = rgb[1];
					  data[i+2] = rgb[2];
				  }
				  if(source=="img/button-down.jpg"){
					  if(data[i]>250 && data[i+1]>250 && data[i+2]>250){
						  /*data[i] = rgb[0];
						  data[i+1] = rgb[1];
						  data[i+2] = rgb[2];*/
						  data[i] = 53;
						  data[i+1] = 65;
						  data[i+2] = 74;
					  }
					  else if(data[i]>210 && data[i+1]>210 && data[i+2]>210){
						  /*console.log(data[i]);
						  console.log(data[i+1]);
						  console.log(data[i+2]);*/
						  data[i] = rgb[0];
						  data[i+1] = rgb[1];
						  data[i+2] = rgb[2];
					  }
					  else if(data[i]>175 && data[i+1]>175 && data[i+2]>175){
						  data[i] = rgb[0]*.65;
						  data[i+1] = rgb[1]*.65;
						  data[i+2] = rgb[2]*.65;
					  }
					  else{
						  data[i] = rgb[0];
						  data[i+1] = rgb[1];
						  data[i+2] = rgb[2];
					  }
				  }
			  }
			  ctx.putImageData(image,0,0);
		  }
		  color();
	  }
	  function clearCanvases(){
		  var topCanvas = document.getElementById("topCanvas");
		  var topCtx = topCanvas.getContext("2d");
		  topCtx.beginPath();
		  topCtx.fillStyle="rgb(53,65,74)";
		  topCtx.rect(0,0,topCanvas.width,topCanvas.height);
		  topCtx.fill();
		  topCtx.closePath();
		  var bottomCanvas = document.getElementById("btmCanvas");
		  var bottomCtx = bottomCanvas.getContext("2d");
		  bottomCtx.beginPath();
		  bottomCtx.fillStyle="rgb(53,65,74)";
		  bottomCtx.rect(0,0,bottomCanvas.width,bottomCanvas.height);
		  bottomCtx.fill();
		  bottomCtx.closePath();
	  }
  });
  $(document).ajaxStop(function(evt){
	  if(suggs!=undefined){
		  clearCanvases();
		  if(suggs.garb.length!=0){
			  var suggestion = newSugg();
			  var bottom_img_src = (suggestion[1].weight == "long")?"img/pants.png":"img/shorts.png";
			  var top_img_src = getTopSrc();
			  var accessory_img_src = getImgSrc();
			  var btmImg = new Image();
			  btmImg.src = bottom_img_src;
			  btmImg.onload = function(){
				  drawImg(this,colors[suggestion[1].color],"btmCanvas",bottom_img_src);
			  }
			  var topImg = new Image();
			  topImg.src = top_img_src;
			  topImg.onload = function(){
				  drawImg(this,colors[suggestion[0].color],"topCanvas",top_img_src);
			  }
			  $("#topCanvas").attr("data-content",suggestion[0].name);
			  $("#btmCanvas").attr("data-content",suggestion[1].name);
			  $("#accessory-img").attr("src",accessory_img_src);
			  accessory_img_size = (accessory_img_src==null)?0:85;
			  $("#accessory-img").attr("height",accessory_img_size);
			  $("#accessory-img").attr("width",accessory_img_size);
		  }
		  else{
			  $("#outfit-wrap").addClass("noshow");
			  $("#formality-section").addClass("noshow");
			  $("#suggest-clothes").removeClass("noshow");
		  }
	  }
	  /*var suggestion = newSugg();
	  var bottom_img_src = (suggestion[1].weight == "long")?"img/pants.png":"img/shorts.png";
	  var bottom_img_size = (suggestion[1].weight == "long")?110:80;
	  var top_img_src = getTopSrc();
	  var accessory_img_src = getImgSrc();
	  var btmImg = new Image();
	  btmImg.src = bottom_img_src;
	  btmImg.onload = function(){
		  drawImg(this,205,200,177);
	  }*/
	  populate();
	  function populate(){
		  city = city.replace(/[_-]/g, " ");
		  $("#curr-loc").text(""+city+", "+state+"");
		  $("#high-low").text(""+forecast[0].high+"\xB0"+" | "+forecast[0].low+"\xB0"+"");
		  $("#feels").text(""+Math.floor(currObs.feelsLike)+"\xB0");
		  $("#precip").text(""+currObs.precip+"%");
		  $("#wind").text(currObs.wind+" MPH");
		  $("#humid").text(currObs.humid);
		  $("#uv").text(currObs.uv);
		  $("#today-temp-div").text(""+currObs.tempF+"\xB0");
		  $("#today-img").attr("src",currObs.iconUrl);
		  $(".day-temp-f").each(function(i){
			  if(forecast[i]){
			  	$(this).text(""+forecast[i].high+"\xB0");
		  	  }
		  });
		  $(".tile-header").each(function(i){
			  if(forecast[i]){
			  	$(this).text(days[forecast[i].weekday]);
		  	  }
		  });
		  $(".day-temp-c").each(function(i){
			  if(forecast[i]){
				  $(this).text(""+forecast[i].low+"\xB0");
			  }
		  });
		  $(".day-img").each(function(i){
			  if(forecast[i]){
				  $(this).attr("src", forecast[i].icon);
			  }
		  });
	  }
	  function drawImg(img,rgb,canvasId,source){
		  var canvas = document.getElementById(canvasId);
		  var ctx = canvas.getContext("2d");
		  ctx.drawImage(img,0,0,100,120);
		  var image = ctx.getImageData(0,0,canvas.width,canvas.height);
		  var data = image.data;
		  var color = function(){
			  for(var i = 0; i<data.length;i+=4){
				  if(data[i]<10 && data[i+1]<10 && data[i+2]<10){
					  data[i] = rgb[0];
					  data[i+1] = rgb[1];
					  data[i+2] = rgb[2];
				  }
				  if(source=="img/button-down.jpg"){
					  if(data[i]>250 && data[i+1]>250 && data[i+2]>250){
						  /*data[i] = rgb[0];
						  data[i+1] = rgb[1];
						  data[i+2] = rgb[2];*/
						  data[i] = 53;
						  data[i+1] = 65;
						  data[i+2] = 74;
					  }
					  else if(data[i]>210 && data[i+1]>210 && data[i+2]>210){
						  /*console.log(data[i]);
						  console.log(data[i+1]);
						  console.log(data[i+2]);*/
						  data[i] = rgb[0];
						  data[i+1] = rgb[1];
						  data[i+2] = rgb[2];
					  }
					  else if(data[i]>175 && data[i+1]>175 && data[i+2]>175){
						  data[i] = rgb[0]*.65;
						  data[i+1] = rgb[1]*.65;
						  data[i+2] = rgb[2]*.65;
					  }
					  else{
						  data[i] = rgb[0];
						  data[i+1] = rgb[1];
						  data[i+2] = rgb[2];
					  }
				  }
			  }
			  ctx.putImageData(image,0,0);
		  }
		  color();
	  }
	  function getTopSrc(){
		  if(suggestion[0].layer=="base"){
			  if(suggestion[0].weight=="short"){
				  return "img/shirt.png";
				  //return "img/light-jacket.png";
			  }
			  else{
				  return "img/button-down.jpg";
				  //return "img/shirt.png";
			  }
		  }
		  else if(suggestion[0].layer=="mid"){
			  return "img/sweater.png";
		  }
		  else{
			  if(suggestion[0].weight="heavy"){
				  //return "img/heavy-jacket.png";
				  return "img/light-jacket.png";
			  }
			  else{
				  return "img/light-jacket.png";
			  }
		  }
	  }
	  function getImgSrc(){
		  if(isRaining()){
			  return "img/umbrella.png";
		  }
		  else if(isFreezing()){
			  return "img/hat.png";
		  }
		  else if(isSunny()){
			  return "img/shades.png";
		  }
		  else{
			  return null;
		  }
	  }
	  function clearCanvases(){
		  var topCanvas = document.getElementById("topCanvas");
		  var topCtx = topCanvas.getContext("2d");
		  topCtx.beginPath();
		  topCtx.fillStyle="rgb(53,65,74)";
		  topCtx.rect(0,0,topCanvas.width,topCanvas.height);
		  topCtx.fill();
		  topCtx.closePath();
		  var bottomCanvas = document.getElementById("btmCanvas");
		  var bottomCtx = bottomCanvas.getContext("2d");
		  bottomCtx.beginPath();
		  bottomCtx.fillStyle="rgb(53,65,74)";
		  bottomCtx.rect(0,0,bottomCanvas.width,bottomCanvas.height);
		  bottomCtx.fill();
		  bottomCtx.closePath();
	  }
  });