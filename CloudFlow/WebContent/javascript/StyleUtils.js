var StyleUtils = {
		
	setFixedPosition : function(jqueryWrapper, pixelsFromTop, pixelsFromLeft) {
		
		jqueryWrapper.css('position', 'absolute');
		
		var setOffsets = function() {
	        jqueryWrapper.css("top", (($(window).scrollTop() + pixelsFromTop) + "px"));
	        jqueryWrapper.css("left", (($(window).scrollLeft() + pixelsFromLeft) + "px"));
	    };

	    setOffsets();

	    $(window).scroll(function() {
	        setOffsets();
	    });
	},
	
	setMinWidth : function(jqueryWrapper, minWidth) {
		jqueryWrapper.each(function() {
			var elementWrapper = $(this);
			if (elementWrapper.width() < minWidth) {
				elementWrapper.width(minWidth);
			}
		})
	}
}