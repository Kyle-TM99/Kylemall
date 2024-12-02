$(document).ready(function() { 
	
	$("#payBtnZipcode").click(payFindZipcode);
	
	
	
	
})

function payFindZipcode() {
	new daum.Postcode({
        oncomplete: function(data) {
            var addr = ''; // 주소 변수
            var extraAddr = ''; // 참고 항목 변수

            //사용자가 선택한 주소 타입과 상관없이 모두 도로명 주소로 처리            
            addr = data.roadAddress;			
            
            if(data.bname !== '' && /[동|로|가]$/g.test(data.bname)){
                extraAddr += data.bname;
            }
            // 건물명이 있고, 공동주택일 경우 추가한다.
            if(data.buildingName !== '' && data.apartment === 'Y'){
                extraAddr += (extraAddr !== '' ? 
										', ' + data.buildingName : data.buildingName);
            }
            
            // 표시할 참고 항목이 있을 경우, 괄호까지 추가한 최종 문자열을 만든다.
            if(extraAddr !== ''){
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