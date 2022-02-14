package com.kosmo.rest.controller;

import java.io.File;

/*
  Swagger
  -https://springfox.github.io/springfox/docs/current/
  -OAS(Open Api Specification)를 위한 오픈 프레임워크이다 즉 OpenAPI에대한 사용설명서를 만들기위한
   프레임워크이다
  -API 자체에 대한 스펙은 컨트롤러에서 작성 
  -Swagger 버전에 따른 URL
	2.X  :  http://localhost:서버포트/컨텍스트루트/swagger-ui.html
   
    
     http://example.org/api/v2/api-docs
 */
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import com.kosmo.rest.service.MemberDAO;
import com.kosmo.rest.service.MemberDTO;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
public class RestAPIController {

	@Autowired
	private MemberDAO dao;

	/*
	 * ※server.xml의 path=""로 수정
	 * 
	 * 
	 * POST http://localhost:9090/members/key :key=value 형태로 전송 :jackson-databind가
	 * 작동하지 않음
	 * 
	 * -form태그를 이용해서 전송하거나 <form method="post"
	 * action="http://localhost:9090/members/key"> <input type="text"
	 * name="username"/> <input type="password" name="password"/> <input type="text"
	 * name="name"/> <input type="submit" value="입력"/> </form> -jQuery ajax로 전송시에는
	 * data속성에 key=value 형태로 $.ajax({ url:"http://localhost:9090/members/key",
	 * dataType:"text", type:"post", data:"username=KIM&password=1234&name=홍길동", ~
	 * 
	 * }); -postman으로 전송시에는 Body탭의 x-www-form-urlencoded 선택후 key와 value입력
	 * 
	 */
	@ApiOperation(value = "회원 입력", notes = "key=value쌍으로 테이타를 받아 일반 텍스트를 반환합니다")
	@CrossOrigin
	@PostMapping(value = "/members/key", produces = "text/plain;charset=UTF-8")
	public String insertKeyValue(MemberDTO member) {
		int affected = dao.insert(member);
		return affected + "행이 입력됨(key=value쌍으로 데이타 받기)";
	}

	/*
	 * POST http://localhost:9090/members/json :json으로 데이터 받을때 :jackson-databind가
	 * 작동해서 json으로 받은 데이타를 DTO로 컨버팅 -jQuery ajax로 전송시에는 data속성에 자바스크립트의 객체로 var json
	 * = {"username":"KIM","password":"1234","name":"홍길동"}; $.ajax({
	 * url:"http://localhost:9090/members/json", dataType:"text", type:"post",
	 * data:JSON.stringify(json), contentType:"application/json;charset=UTF-8", ~
	 * 
	 * }); -postman으로 전송시에는 Body탭의 raw 선택 및 json선택후 json형태로 데이터 작성
	 */
	@CrossOrigin
	@PostMapping(value = "/members/json", produces = "text/plain;charset=UTF-8")
	public String insertJsonValue(@RequestBody MemberDTO member) {

		int affected = dao.insert(member);
		return affected + "행이 입력됨(json으로 데이타 받기)";
	}

	/*
	 * GET http://localhost:9090/members :JSON으로 반환 :jackson-databind가 작동해서 DTO를
	 * JSON으로 컨버팅해서 반환 -form태그 혹은 a태그 -jQuery ajax -postman
	 */
	@CrossOrigin
	@GetMapping("/members")
	public List<MemberDTO> selectList() {
		List<MemberDTO> members = dao.selectList();
		return members;
	}

	/*
	 * GET http://localhost:9090/members/{username} :JSON으로 반환 :jackson-databind가
	 * 작동해서 DTO를 JSON으로 컨버팅해서 반환 -form태그 혹은 a태그 -jQuery ajax -postman
	 */
	@CrossOrigin
	@GetMapping("/members/{username}")
	public MemberDTO selectOne(@PathVariable @ApiParam(value = "회원의 아이디", required = true) String username) {
		MemberDTO member = dao.selectOne(username);
		return member;
	}

	// ※PUT이나 DELETE도 데이타는 요청바디에 싣는다
	// 반드시 JSON으로 받는다 @RequestBody MemberDTO dto
	// 예]{
	// "password":"5678",
	// "name":"KIMKILDONG"
	// }
	// PUT http://localhost:9090/memebrs/KIM
	// {
	// "password":"5678",
	// "name":"KIMKILDONG"
	// }
	//
	@CrossOrigin
	@PutMapping("/members/{username}")

	// @ApiImplicitParams({ //파라미터가 여러개인 경우.단,dataType지정해도 파라미터의 데이타 타입이 표시가 안됨.
	// @ApiImplicitParam(name = "username", value = "회원의 아이디",paramType = "URI
	// Parameter"),
	// @ApiImplicitParam(name = "dto", value = "회원 한명의 정보를 저장하는 DTO",paramType =
	// "JSON Parameter")
	// })
	public MemberDTO update(@PathVariable String username, @RequestBody MemberDTO dto) {
		dto.setUsername(username);
		dao.update(dto);
		return dao.selectOne(username);
	}

	@CrossOrigin
	@DeleteMapping("/members/{username}")

	public MemberDTO delete(@PathVariable @ApiParam(value = "회원 아이디", required = true) // 파라미터 설명.파라미터가 적은 경우
	String username) {
		MemberDTO dto = dao.selectOne(username);// 삭제전 반환할 DTO얻기
		dao.delete(username);
		return dto;// 삭제된 멤버 반환
	}

	/*
	 * POST http://localhost:9090/file :key=value 형태로 전송
	 * 
	 * 
	 * -form태그를 이용해서 전송하거나 <form method="post" action="http://localhost:9090/images"
	 * enctype="multipart/form-data"> <input type="file" name="attach"/> <input
	 * type="text" name="title"/> * <input type="submit" value="입력"/> </form>
	 * 
	 * 
	 * }); -postman으로 전송시에는 Body탭의 form-data 선택후 key와 value입력 파일인 경우 key입력시 옆에
	 * file선택
	 * 
	 */
	@CrossOrigin
	@PostMapping(value = "/images", produces = "text/plain;charset=UTF-8")
	// attach는 키 즉 파라미터명
	public String upload(@RequestPart MultipartFile attach, HttpServletRequest req)
			throws IllegalStateException, IOException {
		// 서버의 물리적 경로 얻기
		String path = req.getServletContext().getRealPath("/resources");
		// File객체 생성
		File f = new File(path + File.separator + attach.getOriginalFilename());
		// 업로드
		attach.transferTo(f);
		System.out.println("기타 파라미터 받기:" + req.getParameter("title"));
		return "파일 업로드 성공";
	}

	/*
	 * [RestTemplate] -Spring 3.0부터 지원하는 내장 클래스로 스프링 서버에서 REST한 방식으로 HTTP 통신을 하기위한
	 * API -Rest방식으로 다른 서버와 HTTP 통신을 동기 방식으로 쉽게 할수 있는 템플릿 (AsyncRestTemplate는 비동기
	 * 통신) -기본적으로 Connection pool을 사용하지 않아서 많은 요청을 하면 TIME_WAIT로 인해 자원이 점점 부족해져 서비스에
	 * 어려움이 있다
	 * 
	 * -내부적으로 java.net.HttpURLConnection 사용 -요청을 보낼때는 HttpEntity< Map혹은
	 * DTO,HttpHeaders>타입에 요청바디(데이타)와 요청헤더와 설정 -응답을 받을때는 ResponseEntity<Map혹은 DTO>
	 * 
	 */
	// https://jsonplaceholder.typicode.com/서버(Rest API서버)로 HTTP로 요청하기
	@Autowired
	private RestTemplate restTemplate;

	// 먼저 https://www.jsonschema2pojo.org/사이트에서 JSON을 DTO로 변환
	@GetMapping(value = "/photos", produces = "application/json;charset=UTF-8")
	// public Photo[] photos(){//Photo[]배열로 응답 받을때
	// public List<Photo> photos(){//List<Photo>로 받을때
	public List<Map> photos() {// List<Map>로 받을때-DTO 불필요
		// 1.요청헤더 설정용 객체 생성
		HttpHeaders headers = new HttpHeaders();
		// headers.add("Content-Type","application/json;charset=UTF-8");//혹은
		headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
		// headers.add("Authorization", "key=발급받는 키값");//요청헤더에 발급 받는 키값을 전송해야 하는 경우
		// 2.요청 헤더정보등을 담은 HttpEntity객체 생성
		// DTO혹은 Map에는 요청시 서버에 보낼 데이타를 담는다.
		// HttpEntity<DTO혹은 Map> entity = new HttpEntity(DTO혹은 Map객체,headers);
		HttpEntity entity = new HttpEntity(headers);
		// 3.RestTemplate으로 요청 보내기
		// String url="요청URI";
		// 요청 URL에 한글 포함시는 UriComponents로 객체 생성후 사용시는 uri.toString()해서 사용한다
		// UriComponents uri = UriComponentsBuilder.fromHttpUrl(url).build();
		String url = "https://jsonplaceholder.typicode.com/photos";

		// ResponseEntity<Photo[]> response= //배열로 받을때
		// ResponseEntity<List<Map>> response= //ParameterizedTypeReference로 타입 설정시
		ResponseEntity<List> response = // List<Map 혹은 DTO>로 받을때

				restTemplate.exchange(url, // 요청 URI
						HttpMethod.GET, // 요청 메소드
						entity, // HttpEntity(요청바디와 요청헤더)
						// Photo[].class);//응답데이타 타입(배열로 받을때)
						// new ParameterizedTypeReference<List<Map>>() { });
						List.class);// 응답데이타 타입(List<Map 혹은 DTO>로 받을때)

		System.out.println("응답코드:" + response.getStatusCodeValue());
		System.out.println("응답헤더:" + response.getHeaders());
		System.out.println("실제 데이터:" + response.getBody());
		System.out.println(response);
		return response.getBody();
	}////////////
		// [안드로이드 앱과 통신용 API 추가]
		// FCM 서비스 : 토큰값 받아서 데이타베이스에 저장

	@CrossOrigin
	@PostMapping(value = "/token", produces = "text/plain;charset=utf-8")
	public String insertToken(@RequestParam String token) {
		// 데이타베이스에 받은 토큰 저장
		int affected = dao.insertToken(token);
		return affected == 1 ? "토큰 저장 성공" : "토큰 저장 실패";
	}

	// 회원여부 판단용
	@CrossOrigin
	@GetMapping("/membership")
	public MemberDTO isMember(@RequestParam Map map) {
		MemberDTO dto = dao.isMember(map);
		System.out.println("회원여부:" + dto);
		if (dto == null) {
			return new MemberDTO();
		}
		return dto;
	}

	@CrossOrigin
	@GetMapping("/photoall")
	public List<Map> photoall() {
		List<Map> photos = new Vector<>();
		String[] thumbnailUrls = { "https://via.placeholder.com/150/92c952", "https://via.placeholder.com/150/22c952",
				"https://via.placeholder.com/150/ffc952", "https://via.placeholder.com/150/66c952",
				"https://via.placeholder.com/150/77c952", "https://via.placeholder.com/150/aac952" };
		int i = 1;
		for (String thumbnailUrl : thumbnailUrls) {
			Map map = new HashMap();
			map.put("thumbnailUrl", thumbnailUrl);
			map.put("title", i++ + "번째 이미지");
			photos.add(map);
		}
		return photos;
	}

	@CrossOrigin
	@PostMapping(value = "/photone", produces = "text/plain; charset=UTF-8")
	public String uploads(@RequestPart MultipartFile attachFile, HttpServletRequest req) throws IllegalStateException, IOException {
		// 서버의 물리적 경로 얻기
		String path = req.getServletContext().getRealPath("/resources");
		// File객체 생성
		File f = new File(path + File.separator + attachFile.getOriginalFilename());
		// 업로드
		attachFile.transferTo(f);
		System.out.println("기타 파라미터 받기:" + req.getParameter("title"));
		return "파일 업로드 성공";
	}
}
