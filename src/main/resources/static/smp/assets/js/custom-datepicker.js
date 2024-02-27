
 (function () {
	 const MONTH = {
				January: 1,
				February: 2,
				March: 3,
				April: 4,
				May: 5,
				June: 6,
				July: 7,
				August: 8,
				September: 9,
				October: 10,
				November: 11,
				December: 12
			}
			
	window.SearchFrom = {
		onOpen: function(targetDate){
			var dateList = $(targetDate).find(".mp-picker-choose > a");
			var currentDay = new Date().getDate();

			for(var i = 0; i<dateList.length; i++){
				if(dateList[i].innerText > currentDay){
					dateList[i].style.pointerEvents = "none";
					dateList[i].style.color = "gray";
				}
			}
		},
		onChange: function(targetDate){
			var splitDate = $(targetDate).find(".mp-picker-site > span")[0].innerText.split(' ');
			var targetYear = splitDate[1];
			var targetMonth = splitDate[0];
			
			var now = new Date();
			var currentYear = now.getFullYear();
			var currentMonth = now.getMonth() + 1;
			
			if(MONTH[targetMonth] == currentMonth){
				var currentDay = new Date().getDate();
				var dateList = $(targetDate).find(".mp-picker-choose > a");
				for(var i = 0; i<dateList.length; i++){
					if(Number(dateList[i].innerText) > currentDay){
						dateList[i].style.pointerEvents = "none";
						dateList[i].style.color = "gray";
					}
				}
			}

			if(MONTH[targetMonth] > currentMonth || Number(targetYear) > currentYear){
				var dateList = $(targetDate).find(".mp-picker-choose > a");
				for(var i = 0; i<dateList.length; i++){
					dateList[i].style.pointerEvents = "none";
					dateList[i].style.color = "gray";
				}
			}
		}
	};
	
	window.SearchTo = {
		onOpen: function(targetDate, prevDate){
			var dateList = $(targetDate).find(".mp-picker-choose > a");
			
			var now = new Date();
			var currentMonth = now.getMonth() + 1;
			var currentDay = now.getDate();
			
			var prevMonth = prevDate[1];
			var prevDay = prevDate[2];
			
			for(var i = 0; i<dateList.length; i++){
				// 오늘보다 큰 숫자는 비활성화
				if(Number(dateList[i].innerText) > currentDay){
					dateList[i].style.pointerEvents = "none";
					dateList[i].style.color = "gray";
				} 
				// from에서 선택한 월과 현재월이 같고, from에서 선택한 날보다 작은 날은 비활성화
				if(Number(dateList[i].innerText) < Number(prevDay) && currentMonth == prevMonth){
					dateList[i].style.pointerEvents = "none";
					dateList[i].style.color = "gray";
				}
			}
		},
		onChange: function(targetDate, prevDate){
			var splitDate = $(targetDate).find(".mp-picker-site > span")[0].innerText.split(' ');
			var targetYear = splitDate[1];
			var targetMonth = splitDate[0];
			
			var now = new Date();
			var currentYear = now.getFullYear();
			var currentMonth = now.getMonth() + 1;
			
			var prevMonth = prevDate[1];
			var prevDay = prevDate[2];
			
			// from에서 선택한 월과 같을 경우 from에서 선택한 일보다 작은 숫자 비활성화 
			if(MONTH[targetMonth] == prevMonth){
				var dateList = $(targetDate).find(".mp-picker-choose > a");
				for(var i = 0; i<dateList.length; i++){
					if(Number(dateList[i].innerText) < prevDay){
						dateList[i].style.pointerEvents = "none";
						dateList[i].style.color = "gray";
					}
				}
			}
			// from에서 선택한 월과 보다 선택한 월이 작은 경우 전체 비활성화
			if(MONTH[targetMonth] < prevMonth){
				var dateList = $(targetDate).find(".mp-picker-choose > a");
				for(var i = 0; i<dateList.length; i++){
					dateList[i].style.pointerEvents = "none";
					dateList[i].style.color = "gray";
				}
			}
			// 현재 년,월 보다 큰 숫자는 전체 비활성화 - 오늘 이후의 데이터는 없기 때문
			if(MONTH[targetMonth] > currentMonth || targetYear > currentYear){
				var dateList = $(targetDate).find(".mp-picker-choose > a");
				for(var i = 0; i<dateList.length; i++){
					dateList[i].style.pointerEvents = "none";
					dateList[i].style.color = "gray";
				}
			}
		}
	};
})();