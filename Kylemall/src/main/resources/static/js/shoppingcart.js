$(document).ready(function () {
    // 수량 변경 시 이벤트 핸들러
    $("input[type='number']").on("input", function () {
        updateTotalPrice();
    });

    // 초기 총 금액 계산
    updateTotalPrice();
	
	// 장바구니 추가 시 이벤트 핸들러
	$("#shoppingCart").click(function() {
		let currentQuantity = $("#shoppingCartQuantity").val()
		
		$.ajax
		
	})
	
	
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
