$(document).ready(function() {

	$("#payBtnZipcode").click(payFindZipcode);

	$('#orderListPayment').on('click', function(e) {
		e.preventDefault(); // 기본 폼 제출 방지

		// 결제 방법 가져오기
		const paymentMethod = $('input[name="radio_paymethod"]:checked').val();

		// 입력된 배송지 정보 가져오기
		const orderData = {
			deliveryName: $('#deliveryName').val(),
			payPhone: $('#payPhone1').val() + '-' + $('#payPhone2').val() + '-' + $('#payPhone3').val(),
			emergencyPhone: $('#emergency1').val() + '-' + $('#emergency2').val() + '-' + $('#emergency3').val(),
			payZipcode: $('#payZipcode').val(),
			payAddress1: $('#payAddress1').val(),
			payAddress2: $('#payAddress2').val(),
			orderMessage: $('#orderMessage').val(),
			deliveryMessage: $('#deliveryMessage').val(),
			paymentMethod: paymentMethod,
			totalPrice: $('#totalLastPrice').text().replace('원', '').trim() // 금액에서 '원' 제거
		};

		if (paymentMethod === 'K') {
			// 카카오페이 결제 호출
			openKakaoPay(orderData);
		} else {
			// 신용카드 결제 처리
			alert('신용카드 결제 진행');
			processCreditCard(orderData);
		}
	});


})

// 카카오페이 결제 메서드
function openKakaoPay(orderData) {
	// 카카오페이 결제 처리 로직
	alert('카카오페이 결제 창을 엽니다.');
	// AJAX를 통해 서버에 결제 정보 전송
	$.ajax({
		url: '/kakaoPay', // 서버의 카카오페이 결제 처리 URL
		type: 'POST',
		data: JSON.stringify(orderData),
		contentType: 'application/json',
		success: function(response) {
			// 결제 성공 후 데이터 저장 요청
			saveOrderData(orderData);
		},
		error: function() {
			alert('결제에 실패했습니다.');
		}
	});
}

// 신용카드 결제 메서드
function processCreditCard(orderData) {
	// 신용카드 결제 처리 로직
	alert('신용카드 결제를 처리합니다.');
	// 결제 성공 후 데이터 저장 요청
	saveOrderData(orderData);
}

// 결제정보 저장 메서드 
function saveOrderData(orderData) {
	// 주문 데이터 저장 AJAX 요청
	$.ajax({
		url: '/saveOrder', // 주문 데이터 저장 URL
		type: 'POST',
		data: JSON.stringify(orderData),
		contentType: 'application/json',
		success: function(response) {
			alert('주문이 성공적으로 완료되었습니다.');
			// 주문 완료 후 페이지 이동 또는 업데이트
		},
		error: function() {
			alert('주문 저장에 실패했습니다.');
		}
	});
}

function payFindZipcode() {
	new daum.Postcode({
		oncomplete: function(data) {
			var addr = ''; // 주소 변수
			var extraAddr = ''; // 참고 항목 변수

			//사용자가 선택한 주소 타입과 상관없이 모두 도로명 주소로 처리            
			addr = data.roadAddress;

			if (data.bname !== '' && /[동|로|가]$/g.test(data.bname)) {
				extraAddr += data.bname;
			}
			// 건물명이 있고, 공동주택일 경우 추가한다.
			if (data.buildingName !== '' && data.apartment === 'Y') {
				extraAddr += (extraAddr !== '' ?
					', ' + data.buildingName : data.buildingName);
			}

			// 표시할 참고 항목이 있을 경우, 괄호까지 추가한 최종 문자열을 만든다.
			if (extraAddr !== '') {
				extraAddr = ' (' + extraAddr + ')';
			}

			// 조합된 참고 항목을 상세주소에 추가한다.
			addr += extraAddr;


			// 우편번호와 주소 정보를 해당 입력상자에 출력한다.
			$("#payZipcode").val(data.zonecode);
			$("#payAddress1").val(addr);
			$("#old_payAddress").val(data.jibunAddress);

			// 커서를 상세주소 입력상자로 이동한다.
			$("#payAddress2").focus();

		}
	}).open();
}