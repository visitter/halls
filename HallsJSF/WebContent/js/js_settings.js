window.onload = function() {
	var a = document.getElementById("j_username");
	if( a!=null )
		a.focus();
};

function runEvent(e) {
	if (e.keyCode == 13) {
     	submitForm();
	}
}
function submitForm(){
	document.getElementById("formLogin").submit();	
}       	

function navigateTo(url){
	window.location.replace(url);
}


PrimeFaces.locales['bg'] = {
		        closeText: 'затвори',
		        prevText: 'предишен',
		        nextText: 'следващ',
		        currentText: 'текущ',
		        monthNames: ['Януари','Февруари','Март','Април','Май','Юни',
		            'Юли','Август','Септември','Октомври','Ноември','Декември'],
		        monthNamesShort: ['Яну','Фев','Мар','Апр','Май','Юни',
		            'Юли','Авг','Септ','Окт','Ное','Дек'],
		        dayNames: ['Неделя','Понеделник','Вторник','Сряда','Четвъртък','Петък','Събота'],
		        dayNamesShort: ['НД','ПН','ВТ','СР','ЧТ','ПТ','СБ'],
		        dayNamesMin: ['Н','П','В','С','Ч','Пт','Сб'],
		        weekHeader: 'седмица',
		        firstDay: 1,
		        isRTL: false,
		        showMonthAfterYear: false,
		        yearSuffix: '',
		        month: 'Месец',
		        week: 'Седмица',
		        day: 'Ден',
		        allDayText : 'цял ден'
		    };
 
 PrimeFaces.locales['bg_short'] = {
	        closeText: 'Х',
	        prevText: '<',
	        nextText: '>',
	        currentText: '&darr;',
	        monthNames: ['Jan','Feb','Mar','Apr','May','Jun',
	            'Jul','Aug','Sep','Oct','Nov','Dec'],
	        monthNamesShort: ['Jan','Feb','Mar','Apr','May','Jun',
	          	            'Jul','Aug','Sep','Oct','Nov','Dec'],
	        dayNames: ['Su','M','T','W','Th','F','S'],
	        dayNamesShort: ['Su','M','T','W','Th','F','S'],
	        dayNamesMin: ['Su','M','T','W','Th','F','S'],
	        weekHeader: 'w',
	        firstDay: 1,
	        isRTL: false,
	        showMonthAfterYear: false,
	        yearSuffix: '',
	        month: 'M',
	        week: 'W',
	        day: 'D',
	        allDayText : 'WD'
	    };