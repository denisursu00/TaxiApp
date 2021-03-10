/**
 * Manager care se ocupa cu notificarea si restrictionarea utilizatorului atunci cand se incarca sau se asteapta
 * date de pe server
 * 
 * @author Nicolae Albu
 * @version 2011.03.23
 */
var LoadingManager = {
		
	ID_LOADING_COURTAIN_DIV : 'loadingCourtain',
	ID_LOADING_TEXT_DIV : 'loadingText',
	
	DEFAULT_LOADING_TEXT : 'Loading...',
	
	loadings : 0,
	
	/**
	 * Notifica aplicatia cum ca va incepe o operatie de incarcare.
	 * Utilizatorul va fi notificat.
	 * Accesul utilizatorului va fi restrictionat pana cand toate incarcarile s-au terminat.
	 */
	loading : function() {
		
		this.loadings++;
		
		document.getElementById(this.ID_LOADING_COURTAIN_DIV).style.display = 'block';
		document.getElementById(this.ID_LOADING_TEXT_DIV).style.display = 'block';
	},
	
	/**
	 * Notifica aplicatia cum ca s-a terminat o operatie de incarcare.
	 * Cand s-au terminat toate operatiile, utilizatorului ii va reveni accesul la interfata grafica.
	 */
	loadingComplete : function() {
		
		this.loadings--;
		
		if (this.loadings == 0) {
			document.getElementById(this.ID_LOADING_COURTAIN_DIV).style.display = 'none';
			document.getElementById(this.ID_LOADING_TEXT_DIV).style.display = 'none';
		} else if (this.loadings < 0) {
			alert('Eroare de programare! Contactati echipa de suport si precizati ultimele actiuni pe care le-ati facut.');
		}
	},
	
	/**
	 * Activeaza functionalitatea manager-ului pentru pagina curenta.
	 * Textul care va fi afisat utilizatorului in timp ce sunt operatii in derulare va fi cel implicit.
	 */
	enable : function() {
		this.enableWithText(this.DEFAULT_LOADING_TEXT);
	},

	/**
	 * Activeaza functionalitatea manager-ului pentru pagina curenta.
	 * 
	 * @param text {String} textul care va fi afisat utilizatorului in timp ce sunt operatii in derulare
	 */
	enableWithText : function(text) {
		
		var body = document.getElementsByTagName('body').item(0);
		
		var loadingCourtain = document.getElementById(this.ID_LOADING_COURTAIN_DIV);
		var loadingText = document.getElementById(this.ID_LOADING_TEXT_DIV);
		
		if ((loadingCourtain != null) && (loadingText != null)) {
			return;
		}
		
		loadingCourtain = document.createElement('div');
		loadingCourtain.setAttribute('id', this.ID_LOADING_COURTAIN_DIV);
		
		loadingText = document.createElement('div');
		loadingText.setAttribute('id', this.ID_LOADING_TEXT_DIV);
		loadingText.innerHTML = text;
		
		body.appendChild(loadingCourtain);
		body.appendChild(loadingText);
	}
};