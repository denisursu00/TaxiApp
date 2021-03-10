var ScrollUtils = {
	
	doSomethingThatAffectsScrollPosition : function(affectingScrollPositionFunction) {
		
		var oldDocumentWidth = $(document).width();
		var oldScrollFromLeft = $(window).scrollLeft();
		
		var oldDocumentHeight = $(document).height();
	    var oldScrollFromTop = $(window).scrollTop();
	    
	    affectingScrollPositionFunction();
		
		var newDocumentWidth = $(document).width();
		var widthRatio = (newDocumentWidth / oldDocumentWidth);
		var newScrollFromLeft = (oldScrollFromLeft * widthRatio);
		
		var newDocumentHeight = $(document).height();
		var heightRatio = (newDocumentHeight / oldDocumentHeight);
		var newScrollFromTop = (oldScrollFromTop * heightRatio);

		$(window).scrollLeft(0);
		$(window).scrollLeft(newScrollFromLeft);
		
		$(window).scrollTop(0);
		$(window).scrollTop(newScrollFromTop);
	}
};