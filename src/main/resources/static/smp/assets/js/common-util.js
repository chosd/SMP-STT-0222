/**
기본적으로 많이 사용되는 기능을 함수로 제공함.
namespace : fn
author : ku.ji@kt.com
date : 2022.09.14
 */
 (function () {
	window.fn = {
		/**
		 입력된 수자에 3자리수 마다 ,를 추가한 문자열을 반환
		 예> 10000 => 10,000
		 */
		addComma: function(num) {
			var regexp = /\B(?=(\d{3})+(?!\d))/g;
  			return num.toString().replace(regexp, ',');
		},
		
		/**
		입력된 html status code에 대한 상태 메시지를 제공한다.
		예> 200 => "Success" or "성공"
		 */
		getStatusMessage: function (code) {
			var message = "unknown";
			switch(code + "") {
				case "200": message = "성공"; break;
				case "400": message = "잘못된 요청입니다. 확인 후 다시 요청해 주세요."; break;
				case "401": message = "로그인이 필요합니다. 로그인 후 다시 요청해 주세요."; break;
				case "403": message = "사용할 수 없는 기능입니다. 관리자에게 문의하시기 바랍니다."; break;
				case "404": message = "잘못된 요청입니다. 확인 후 다시 요청해 주세요."; break;
				case "405": message = "잘못된 요청입니다. 확인 후 다시 요청해 주세요."; break;
				case "406": message = "잘못된 요청입니다. 확인 후 다시 요청해 주세요."; break;
				case "408": message = "일시적인 오류가 발생하였습니다. 잠시 후 다시 요청해 주세요. 지속적으로 동일한 문제가 발생하는 경우 관리자에게 문의하시기 바랍니다."; break;
				case "409": message = "잘못된 요청입니다. 확인 후 다시 요청해 주세요."; break;
				case "410": message = "잘못된 요청입니다. 확인 후 다시 요청해 주세요."; break;
				case "413": message = "요청 또는 파일의 크기가 너무 큽니다. 확인 후 다시 요청해 주세요."; break;
				case "415": message = "지원하지 않는 요청 또는 파일 타입입니다. 확인 후 다시 요청해 주세요."; break;
				case "423": message = "요청된 내용은 현재 잠겨 있습니다. 관리자에게 문의하시기 바랍니다."; break;
				case "424": message = "잘못된 요청입니다. 확인 후 다시 요청해 주세요."; break;
				case "429": message = "승인된 사용량을 모두 사용하였습니다. 관리자에게 문의하시기 바랍니다."; break;
				case "432": message = "잘못된 요청입니다. 확인 후 다시 요청해 주세요."; break;
				case "433": message = "잘못된 요청입니다. 확인 후 다시 요청해 주세요."; break;
				case "434": message = "잘못된 요청입니다. 확인 후 다시 요청해 주세요."; break;
				case "500": message = "내부 오류가 발생하였습니다. 관리자에게 문의하시기 바랍니다."; break;
				case "501": message = "내부 오류가 발생하였습니다. 관리자에게 문의하시기 바랍니다."; break;
				case "502": message = "내부 오류가 발생하였습니다. 관리자에게 문의하시기 바랍니다."; break;
				case "503": message = "내부 오류가 발생하였습니다. 관리자에게 문의하시기 바랍니다."; break;
				case "504": message = "일시적인 오류가 발생하였습니다. 잠시 후 다시 요청해 주세요. 지속적으로 동일한 문제가 발생하는 경우 관리자에게 문의하시기 바랍니다."; break;
				default:
				
			}
			
			return message;
		}
	};

	window.commonObject = {
		deleteNullOrDefaultFields: function (obj) {
	        for (var key of Object.keys(obj)) {
	            if (obj[key] == null || obj[key] == undefined) {
	                delete obj[key];
	            }
	            if (typeof obj[key] == "number" && obj[key] == 0) {
	                delete obj[key];
	            }
	            if (typeof obj[key] == "string" && obj[key] == '') {
	                delete obj[key];
	            }
	        }
	    }
	}
	window.SessionUtils = {

	    getKeyName: function () {
	        return "BULK_KEY";
	    },

	    getBulkKey: function () {
	        return sessionStorage.getItem(this.getKeyName());
	    },

	    setBulkKey: function (key) {
	        sessionStorage.setItem(this.getKeyName(), key);
	    },

	    clearBulkKey: function () {
	        sessionStorage.setItem(this.getKeyName(), "");
	    }
	};

})();