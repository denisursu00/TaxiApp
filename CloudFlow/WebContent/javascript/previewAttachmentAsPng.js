$(document).ready(function() {
	
	StyleUtils.setFixedPosition($('#zoomButtons'), 20, 20);
	StyleUtils.setMinWidth($('#zoomButtons button'), 60);
	
	var imagesJquerySelector = '.pagePreviewImage';
	
	var zoomInRatio = 1.20;
	var zoomOutRatio = 0.80;	
	
	$('#zoomFitButton').click(function() {
		ScrollUtils.doSomethingThatAffectsScrollPosition(function() {
			$(imagesJquerySelector).each(function() {
				
				var image = $(this);
				
				image.css('width', '');
				image.css('height', '');
				
				image.css('width', '100%');
			});
		})
	});
	
	$('#zoomInButton').click(function() {
		ScrollUtils.doSomethingThatAffectsScrollPosition(function() {
			$(imagesJquerySelector).each(function() {
				
				var image = $(this);
				
				var oldWidth = image.width();
				var oldHeight = image.height();
				
				var newWidth = (oldWidth * zoomInRatio);
				var newHeight = (oldHeight * zoomInRatio);
				
				image.width(newWidth);
				image.height(newHeight);
			});
		});
	});
	
	$('#zoomOutButton').click(function() {
		ScrollUtils.doSomethingThatAffectsScrollPosition(function() {
			$(imagesJquerySelector).each(function() {
				
				var image = $(this);
				
				var oldWidth = image.width();
				var oldHeight = image.height();
				
				var newWidth = (oldWidth * zoomOutRatio);
				var newHeight = (oldHeight * zoomOutRatio);
				
				image.width(newWidth);
				image.height(newHeight);
			});
		});
	});
});