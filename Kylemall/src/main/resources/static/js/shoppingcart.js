$(document).ready(function () {
    // 수량 변경 시 이벤트 핸들러
    $("input[type='number']").on("input", function () {
        updateTotalPrice();
    });

    // 초기 총 금액 계산
    updateTotalPrice();
	
	// 장바구니 추가 시 이벤트 핸들러
	$("#shoppingCart").click(function () {
	    // 현재 장바구니에 있는 상품 종류 수
	    let currentQuantity = $("#shoppingCartQuantity").val(); // 장바구니 현재 개수

	    // 추가할 상품의 개수
	    let cnt = $("#productCount").val(); // 추가할 상품 수량 (입력 필드)

	    // 사용자 ID
	    let memberId = $("#memberId").val(); // 사용자 ID (hidden 필드)

	    // 상품 번호
	    let productNo = $("#productNo").val(); // 상품 번호 (hidden 필드)

	    // 유효성 검사
	    if (!cnt || !productNo || !memberId) {
	        alert("필수 정보를 확인해주세요.");
	        return;
	    }

	    // AJAX 요청
	    $.ajax({
	        url: "/cart/add", // 요청을 처리할 서버의 URL
	        method: "POST",   // HTTP 메서드
	        data: {
	            memberId: memberId,
	            productNo: productNo,
	            count: cnt,
	        },
	        success: function (response) {
	            // 요청 성공 시
	            alert("장바구니에 상품이 추가되었습니다!");
	            // 장바구니 수량 업데이트
	            $("#shoppingCartQuantity").val(response.updatedQuantity);
	        },
	        error: function (xhr, status, error) {
	            // 요청 실패 시
	            alert("장바구니 추가에 실패했습니다.");
	            console.error(error);
	        },
	    });
	});
	
	
	// 총 결제 금액 업데이트 함수
    function updateTotalPrice() {
        let totalPrice = 0;

        // 각 상품의 수량과 가격 계산
        $(".table tr").each(function () {
            const quantity = $(this).find("input[type='number']").val(); // 수량
            const priceElement = $(this).find(".product-price"); // 가격 정보
            const price = parseInt(priceElement.attr("data-price")); // 가격 가져오기

            if (!isNaN(price) && !isNaN(quantity)) {
                totalPrice += price * quantity;
            }
        });

        // 총 결제 금액 반영
        $("#totalPrice").text(`${totalPrice.toLocaleString()}원`);
    }
});
