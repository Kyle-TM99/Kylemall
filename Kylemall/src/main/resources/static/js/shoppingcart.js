$(function() { 
	
	$("#shoppingCart").click(function() {
			var quantity = $("#shoppingCartQuantity").val();

			$.ajax({
				"url": "passCheck.ajax",
				"type": "get",
				"data": data,
				"dataType": "json",
				"success": function(resData) {				
					if(resData.result) {
						alert("비밀번호가 확인되었습니다.\n비밀번호를 수정해주세요");
						$("#btnPassCheck").prop("disabled", true);
						$("#oldPass").prop("readonly", true);
						$("#pass1").focus();
						
					} else {
						alert("비밀번호가 틀립니다.\n비밀번호를 다시 확인해주세요");
						$("#oldPass").val("").focus();
					}
				},
				"error": function(xhr, status) {
					console.log("error : " + status);
				}
			});		
		});
	
});