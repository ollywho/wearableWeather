var garments;
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
var rgbToString = {};
rgbToString["rgb(255, 255, 255)"] = "white";
rgbToString["rgb(255, 0, 0)"] = "red";
rgbToString["rgb(0, 93, 255)"] = "blue";
rgbToString["rgb(0, 0, 110)"] = "navy";
rgbToString["rgb(139, 69, 19)"] = "brown";
rgbToString["rgb(85, 107, 47)"] = "green";
rgbToString["rgb(255, 165, 0)"] = "orange";
rgbToString["rgb(255, 255, 0)"] = "yellow";
rgbToString["rgb(210, 180, 140)"] = "tan";
rgbToString["rgb(0, 0, 0)"] = "black";
rgbToString["rgb(128, 128, 128)"] = "gray";
rgbToString["rgb(128, 0, 128)"] = "purple";
rgbToString["rgb(139, 0, 0)"] = "burgundy";
rgbToString["rgb(255, 192, 203)"] = "pink";
function addGarment(name, type, layer, weight, waterproof, color, user, formality){
	var postParameters = {name: name, type: type, layer: layer, weight: weight, waterproof: waterproof, color: color, formality: formality, user: user};
	$.post("/add", postParameters,  function(responseJSON){
		returnedObject = JSON.parse(responseJSON);
		var is_success = returnedObject.is_success;
	});
}
				  
function removeGarment(id){
	var postParameters = {id: id};
	$.post("/remove", postParameters,  function(responseJSON){
		returnedObject = JSON.parse(responseJSON);
		var is_success = returnedObject.is_success;
	});
}
				  
function getAllGarments(){
	$.get("/garments",  function(responseJSON){
		returnedObject = JSON.parse(responseJSON);
		garments = returnedObject.garb;
	});
}
$(document).ready(function(){
				getAllGarments();
				$('#clothing-type').on('change', function (e) {
					var optionSelected = $("option:selected", this);
					var valueSelected = this.value;
					console.log(valueSelected);
					if(valueSelected=="Top"){
						$("#form-tops").removeClass("noshow");
						$("#form-bottoms").addClass("noshow");
						$("#form-base-layer").addClass("noshow");
						$("#form-outer-layer").addClass("noshow");					}
					else if(valueSelected=="Bottom"){
						$("#form-bottoms").removeClass("noshow");
						$("#form-tops").addClass("noshow");
					}
				});
				$('#top-layer').on('change', function (e) {
					var optionSelected = $("option:selected", this);
					var valueSelected = this.value;
					if(valueSelected=="Base"){
						$("#form-base-layer").removeClass("noshow");
						$("#form-outer-layer").addClass("noshow");
					}
					else if(valueSelected=="Mid"){
						$("#form-base-layer").addClass("noshow");
						$("#form-outer-layer").addClass("noshow");
					}
					else if(valueSelected=="Outer"){
						$("#form-base-layer").addClass("noshow");
						$("#form-outer-layer").removeClass("noshow");
					}
				});
				$("#color").click(function(){
					$("#color-dropdown").toggleClass("noshow");
				});
				$(".color-block").click(function(){
					$("#color-dropdown").addClass("noshow");
					var color = $(this).css("background-color");
					$("#color").css('background-color', color);
				})
				var hidden = null;
				var terminated = false;
				var activeInterval = null;
				$("#wardrobe-right").on('click','.garment-img',function(){
					if(activeInterval){
						clearInterval(activeInterval);
						activeInterval = null;
						//delete hidden from database
						removeGarment(hidden.attr('id'));
					}
					$('#undo').removeClass("noshow");
					$(this).parent().parent().fadeOut("slow");
					hidden = $(this).parent().parent();
					var count = 4;
					var myInterval = setInterval(function(){
						$('#time').text("..."+count+"");
						count--;
						if(count==-1 || terminated){
						 clearInterval(myInterval);
						 activeInterval = null;
						 //delete hidden from database
						 removeGarment(hidden.attr('id'));
						 terminated = false;
						 $('#undo').addClass("noshow");
						 $('#time').text("..."+5+"");
						}
					},1000);
					activeInterval = myInterval;
				})
				$(".confirm").click(function(){
					$(this).parent().parent().remove();
				});
				$("#revert").click(function(){
					hidden.fadeIn("slow");
					terminated = true;
					activeInterval = null;
				});
				$("#add-btn").click(function(){
					var fields = [];
					$(".var-parent").each(function(i, obj) {
						if($(obj).children(".variable").length==0){
							$(obj).children(".form-group").each(function(j,object){
								$(object).children(".variable").each(function(k,object1){
									if(!$(obj).hasClass("noshow") && !$(object).hasClass("var-parent")){
										fields.push(object1);
									}
								});
							});
						}
						else{
							$(obj).children(".variable").each(function(j,object){
								if(!$(obj).hasClass("noshow")){
									fields.push(object);
								}
							});
						}
					});
					var color = rgbToString[$("#color").css("background-color")];
					var layer = "";
					var weight = "";
					var waterproof = 0;
					for(var i=0;i<fields.length;i++){
						if($(fields[i]).hasClass("weight")){
							weight = $(fields[i]).val().toLowerCase();
						}
						else if($(fields[i]).hasClass("layer")){
							layer = $(fields[i]).val().toLowerCase();
						}
						else if($(fields[i]).hasClass("waterproof")){
							waterproof = ($(fields[i]).val()=="Yes")?1:0;
						}
					}
					var formalVal;
					var sliderVal = $("#myRange").val();
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
					 var name = ($("#name").val().toLowerCase().length >0)?$("#name").val().toLowerCase():"Default Name";
					 var type = ($("#clothing-type").val().toLowerCase() == "top" || $("#clothing-type").val().toLowerCase() == "bottom")?$("#clothing-type").val().toLowerCase():"bottom";
					 weight = (weight.length>0)?weight:"long";
					 console.log("color: "+ color);
					 console.log("name: "+$("#name").val().toLowerCase());
					 console.log("type: "+$("#clothing-type").val().toLowerCase());
					 console.log("layer: "+layer);
					 console.log("weight: "+weight);
					 console.log("waterproof: "+waterproof);
					 console.log("formality: "+formalVal);
					addGarment(name,type, layer, weight, waterproof, color, "penis", formalVal);
				});
			});
$(document).ajaxStop(function(evt){
	var images = [];
	$('#wardrobe-right').empty();
	for(var i = 0; i < garments.length; i++) {
		$('#wardrobe-right').append(
			'<div class="garment-wrap">'+
				'<div id="'+garments[i].id+'" class="garment">'+
					'<div class="col-md-3">'+
						'<canvas id="garment'+garments[i].id+'" class="clothing-pic" height="70" width="70">'+
						'</canvas>'+
					'</div>'+
					'<div class="col-md-3 garment-txt">'+garments[i].name+
					'</div>'+
					'<div class="col-md-3 garment-txt">'+garments[i].type+
					'</div>'+
					'<div class="col-md-3 garment-txt">'+garments[i].weight+
						'<img class="garment-img" src="img/trash.png" height="20" width="20">'+
					'</div>'+
				'</div>'+
			'</div>'
				//src="shirt.png"
				//src="trash.png"
		);
		var idString = "garment"+garments[i].id+"";
		console.log(idString);
		var img_src;
		if(garments[i].type=="top"){
			img_src = getTopSrc(garments[i]);
		}
		else{
			img_src= (garments[i].weight == "long")?"img/pants.png":"img/shorts.png";
		}
		var img = new Image();
		img.src = img_src;
		var color = garments[i].color;
		var imageInfo = {};
		imageInfo["image"] = img;
		imageInfo["color"] = colors[color];
		imageInfo["id"] = idString;
		imageInfo["source"] = img_src;
		images.push(imageInfo);
	}
	for(var i=0;i<images.length;i++){
		drawImg(images[i].image,images[i].color,images[i].id,images[i].source);
	}
		function drawImg(img,rgb,canvasId,source){
			console.log("drawing garment");
			  var canvas = document.getElementById(canvasId);
			  var ctx = canvas.getContext("2d");
			  ctx.drawImage(img,0,0,70,70);
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
							  data[i] = 45;
							  data[i+1] = 56;
							  data[i+2] = 64;
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
		function getTopSrc(garment){
			  if(garment.layer=="base"){
				  if(garment.weight=="short"){
					  return "img/shirt.png";
					  //return "img/light-jacket.png";
				  }
				  else{
					  return "img/button-down.jpg";
					  //return "img/shirt.png";
				  }
			  }
			  else if(garment.layer=="mid"){
				  return "img/sweater.png";
			  }
			  else{
				  if(garment.weight="heavy"){
					  return "img/heavy-jacket.png";
					  //return "img/light-jacket.png";
				  }
				  else{
					  return "img/light-jacket.png";
				  }
			  }
		  }
});