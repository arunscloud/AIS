<!doctype html>
<html lang="en">
<head>
<meta charset="utf-8" />
<title>Agro Information System</title>
<link rel="stylesheet" href="http://code.jquery.com/ui/1.10.3/themes/start/jquery-ui.css" />
<script src="http://code.jquery.com/jquery-1.9.1.js"></script>
<script src="http://code.jquery.com/ui/1.10.3/jquery-ui.js"></script>
<style>
.custom-combobox {
position: relative;
display: inline-block;
}
.custom-combobox-toggle {
position: absolute;
top: 0;
bottom: 0;
margin-left: -1px;
padding: 0;
/* support: IE7 */
*height: 1.7em;
*top: 0.1em;
}
.resizedTextbox {
width: 245px;
height: 20px
}
.custom-combobox-input {
margin: 0;
padding: 0.3em;
}
</style>
<script>
(function( $ ) {
$.widget( "custom.combobox", {
_create: function() {
this.wrapper = $( "<span>" )
.addClass( "custom-combobox" )
.insertAfter( this.element );
this.element.hide();
this._createAutocomplete();
this._createShowAllButton();
},
_createAutocomplete: function() {
var selected = this.element.children( ":selected" ),
value = selected.val() ? selected.text() : "";
this.input = $( "<input>" )
.appendTo( this.wrapper )
.val( value )
.attr( "title", "" )
.addClass( "custom-combobox-input ui-widget ui-widget-content ui-state-default ui-corner-left" )
.autocomplete({
delay: 0,
minLength: 0,
source: $.proxy( this, "_source" )
})
.tooltip({
tooltipClass: "ui-state-highlight"
});
this._on( this.input, {
autocompleteselect: function( event, ui ) {
ui.item.option.selected = true;
this._trigger( "select", event, {
item: ui.item.option
});
},
autocompletechange: "_removeIfInvalid"
});
},
_createShowAllButton: function() {
var input = this.input,
wasOpen = false;
$( "<a>" )
.attr( "tabIndex", -1 )
.attr( "title", "Show All Items" )
.tooltip()
.appendTo( this.wrapper )
.button({
icons: {
primary: "ui-icon-triangle-1-s"
},
text: false
})
.removeClass( "ui-corner-all" )
.addClass( "custom-combobox-toggle ui-corner-right" )
.mousedown(function() {
wasOpen = input.autocomplete( "widget" ).is( ":visible" );
})
.click(function() {
input.focus();
// Close if already visible
if ( wasOpen ) {
return;
}
// Pass empty string as value to search for, displaying all results
input.autocomplete( "search", "" );
});
},
_source: function( request, response ) {
var matcher = new RegExp( $.ui.autocomplete.escapeRegex(request.term), "i" );
response( this.element.children( "option" ).map(function() {
var text = $( this ).text();
if ( this.value && ( !request.term || matcher.test(text) ) )
return {
label: text,
value: text,
option: this
};
}) );
},
_removeIfInvalid: function( event, ui ) {
// Selected an item, nothing to do
if ( ui.item ) {
return;
}
// Search for a match (case-insensitive)
var value = this.input.val(),
valueLowerCase = value.toLowerCase(),
valid = false;
this.element.children( "option" ).each(function() {
if ( $( this ).text().toLowerCase() === valueLowerCase ) {
this.selected = valid = true;
return false;
}
});
// Found a match, nothing to do
if ( valid ) {
return;
}
// Remove invalid value
this.input
.val( "" )
.attr( "title", value + " didn't match any item" )
.tooltip( "open" );
this.element.val( "" );
this._delay(function() {
this.input.tooltip( "close" ).attr( "title", "" );
}, 2500 );
this.input.data( "ui-autocomplete" ).term = "";
},
_destroy: function() {
this.wrapper.remove();
this.element.show();
}
});
})( jQuery );
$(function() {
	var datet;	
	

	$( "#datepicker" ).datepicker({
		onSelect: function(dateText) {
		    //alert("Selected date: " + dateText + "; input's current value: " + this.value);
		    datet=dateText;
		  }	
	});
	
	$( "#buttonGetCrops" ).click(function() {
	
	//alert($('#Regioncombobox').val());
	$.ajax({
	    // type: "GET",
	    type: "GET",
	    contentType: "JSON",
	    url: "./AISServ",
	    data: {region:$('#Regioncombobox').val(),soilPH:$('#soilp').val(),temp:$('#temp').val(),soilType:$('#Soilcombobox').val()},
	    success: function (msg) {
	        for(var len=0;len<msg.crops.length;len++)
	        	{
	        	 $('#dialog').append('</br></br>');
	        	 $('#dialog').append('<h2>'+msg.crops[len]+'</h2>Market Yield is $'+msg.MY+'/Ton</br></br>');
	        	  $('#dialog').append('<a href="./PdfGen?Crop='+msg.crops[len]+"&date="+datet+"&acreage="+$('#acreage').val()+'">Download data for '+msg.crops[len]+'</a></br>');
	        	}	        
	    	//$('#dialog').append('<span>Hi</span>');
	    	
	    }
	});
	
	$( "#dialog" ).dialog( "open" );
    });
	
	
	
$( "#Soilcombobox" ).combobox();
$( "#Regioncombobox" ).combobox();
$('#buttonGetCrops').button();


$('#temp').bind('keyup', function() {
    regex = /^[1-9]([0-9]{1,45}$)/;
    if(!regex.test($(this).val())) {
        $(this).next('.error').remove();
        $(this).after('<div class="error">'+'<font color="red">&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp*Wrong Input</font>'+'</div>');
    } else {
       $(this).next('.error').remove();            
    }
}); 

$('#acreage').bind('keyup', function() {
    regex = /^[1-9]([0-9]{1,45}$)/;
    if(!regex.test($(this).val())) {
        $(this).next('.error').remove();
        $(this).after('<div class="error">'+'<font color="red">&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp*Wrong Input</font>'+'</div>');
    } else {
       $(this).next('.error').remove();            
    }
}); 


    




$( "#dialog" ).dialog({
      autoOpen: false,
	  width: 600,
      height: 500,
      show: {
        effect: "blind",
        duration: 1000
      },
      hide: {
        effect: "explode",
        duration: 1000
      }
    });
});

</script>
</head>
<body>
<center>
<h2> Agro Information System </h2>

</br>
</br>
<div class="ui-widget">
<label>&nbsp&nbsp&nbsp&nbsp&nbsp&nbspSoil Type&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp </label>
<select id="Soilcombobox">
<option value="">Select one...</option>
<option value="Alfisol">Alfisol</option>
<option value="Andisol">Andisol</option>
<option value="Entisol">Entisol</option>
<option value="Gelisol">Gelisol</option>
<option value="Histisol">Histisol</option>
<option value="Spodosol">Spodosol</option>
</select>

</br>
</br>
<label>&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbspRegion&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp </label>
<select id="Regioncombobox">
<option value="">Select one...</option>
<option value="Arizona">Arizona</option>
<option value="California">California</option>
<option value="Nevada">Nevada</option>
<option value="Hawaii">Hawaii</option>
<option value="Texas">Texas</option>
<option value="Florida">Florida</option>
</select>
</br>
</br>
<label for="temp">Temperature (in °F)&nbsp&nbsp&nbsp&nbsp&nbsp </label>
<input type="text" id="temp" class="resizedTextbox" name="Temperature"><br><br>
<label for="soilp">&nbsp&nbsp&nbsp&nbsp Soil PH (in PH)&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp </label>
<input type="text" id="soilp" class="resizedTextbox" name="Soil PH">


</br></br><label for="acreage">&nbsp Acreage (in Acres) &nbsp&nbsp&nbsp&nbsp </label>
<input type="text" id="acreage" class="resizedTextbox" name="Acreage">

</br></br>
<label for="datepicker">&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbspDate&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp </label>
<input type="text" id="datepicker" class="resizedTextbox" name="Date">

</div>

<div id="dialog" width="800" height="800" title="Crops">

</div>

</br>
</br>

<button id="buttonGetCrops">Get Crops</button>

</center>
</body>
</html>