/**
 single page application(SPA)를 제공하기 위한 기본적인 Util를 제공
 namespace : spa
 author : ku.ji@kt.com
 date : 2022.09.14
 */
(function () {

	// 화면 변경 이벤트

	window.spa = {
		rootSelector: null,				// content root

		/**
		 SPA 기능 초기화
		 */
		initialize: function (selector) {
			this.rootSelector = selector;
		},

		/**
		 화면을 이동한다.
		 */
		move: function (url, htmlUrl, cbFunc) {
			if(history.replaceState) {
				if (url != void 0) {
					if(location.pathname == '/') {
						// 맨 처음 page의 경우 history를 변경하여 back 시 이전 페이지로 돌아가도록 한다.
						history.replaceState({}, null, url);
					} else {
						history.pushState({}, null, url);
					}
				} else {
					// 아무런 입력이 없는 경우 기본 경로로 제공
					console.warn("url is undefined.")
					history.replaceState({}, null, location.origin);
				}
			}
			this.view(htmlUrl, cbFunc);
		},

		/**
		 * 화면에 입력된 URL의 html을 제공한다.
		 * @param htmlUrl
		 * @param cbFunc
		 */
		view: function (htmlUrl, cbFunc) {
			var self = this;
			if (htmlUrl != null) {
				this.loadHtml(htmlUrl, function (isError, data) {
					if(isError) {
						if (typeof cbFunc === "function") {
							cbFunc(isError, data);
						} else {
							console.error("window.spa.view", data);
						}
					} else {
						var $root = $(self.rootSelector);
						$root.trigger('page.move');
						//$root.empty();
						var $prePage = $root.children();
						// var $newPage = $("<div class='position-absolute px-3 l0 r0' style='opacity: 0;'></div>").append(data);
						var $newPage = $("<div class='page' style='opacity: 0;'></div>").append(data);
						if (0 < $prePage.length) {
							$prePage.animate({opacity: 0}, 100, function () {
								$prePage.remove();
								$root.append($newPage);
								$newPage.animate({opacity: 1}, 200);
							});
						} else {
							$root.append($newPage);
							$newPage.animate({opacity: 1}, 200);
						}
						if (typeof cbFunc === "function") {
							cbFunc(isError, data);
						}
					}
				});
			} else {
				var $root = $(self.rootSelector);
				$root.trigger('page.move');
				var $prePage = $root.children();
				$prePage.animate({opacity: 0}, 100, function () {
					$prePage.remove();
				});
			}
		},

		/**
		 입력된 url에 대한 html를 요청하여 전달함.
		 */
		loadHtml: function (url, cbFunc) {
			$.ajax({
				method: "GET",
				url: url,
				dataType: "html",
				success: function (response, textStatus, jqXHR) {
					if (typeof cbFunc === "function") {
						cbFunc(false, response);
					}
				},
				error: function (jqXHR, textStatus, errorThrown) {
					if (typeof cbFunc === "function") {
						cbFunc(true, jqXHR.status+"] "+jqXHR.responseText);
					}
				}
			});
		},
	}
})();
 