package kh.spring.fongdang.admin.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import kh.spring.fongdang.admin.domain.Sales;
import kh.spring.fongdang.admin.model.service.AdminServiceImpl;
import kh.spring.fongdang.member.domain.Member;


@Controller
@RequestMapping("/admin")
public class AdminController {
	
	private static final Logger logger = LoggerFactory.getLogger(AdminController.class);
	
	@Autowired
	private AdminServiceImpl service;
	
	@RequestMapping(value="/memberManagement", method= RequestMethod.GET)
	public ModelAndView pageMemberManagement(ModelAndView mv
			, @RequestParam(value="related_search", required=false) String keyword
			, @RequestParam(value="page", defaultValue="nothing") String currentPageStr
			, HttpSession session) {		
		List<Member> memberList =null; 		
		System.out.println("keyword: " + keyword);
		// �α��� ���� Ȯ�� 
//		TODO: ���Ŀ� ������(admin) �α��� Ȯ�� �� �������������� �Ѿ���� �ϱ�
//		Member authInfo = (Member)session.getAttribute("loginInfo");
//		if(authInfo == null) {
//			System.out.println("\n���� �α׾ƿ� �����Դϴ�.");
//			mv.setViewName("redirect:/member/login");
//			return mv;
//		}		
		int currentPage = 1;	
		int memberLimit = 5;
		
		try {
			if(currentPageStr !=null && !currentPageStr.equals("nothing"))
				currentPage = Integer.parseInt(currentPageStr);
		}catch (NumberFormatException e) {
			e.printStackTrace();
		}
		
		final int pageSize = 6;  // ���������� ������ ��
		final int pageBlock = 3;  // ����¡�� ��Ÿ�� ��������
		int startPage=0;
		int endPage=0;
		int startNum=0;
		int endNum=0;
		
		// �� ȸ�� ��
		int totalCnt = 0;
		if(keyword != null) {
			totalCnt = service.countSearchMember(keyword);
		}
		else {
			totalCnt = service.countMember();
		}
		
		System.out.println("\n�� ȸ�� �� :\t" + totalCnt); 
		
		/* Paging ó�� */
		int totalPageCnt = (totalCnt/pageSize) + (totalCnt%pageSize==0 ? 0 : 1);
		if(currentPage%pageBlock == 0) {
			startPage = ((currentPage/pageBlock)-1)*pageBlock + 1;
		} else {
			startPage = (currentPage/pageBlock)*pageBlock + 1;
		}
		endPage = startPage + pageBlock - 1;
		if(endPage>totalPageCnt) {
			endPage = totalPageCnt;
		}
		System.out.println("page:"+ startPage +"~"+endPage);
		
		/* rownum ó�� */
		startNum = (currentPage-1)*pageSize + 1;
		endNum = startNum + pageSize -1;
		if(endNum>totalCnt) {
			endNum = totalCnt;
		}
		System.out.println("rnum:"+ startNum +"~"+endNum);			
		
		if(keyword != null) {
			memberList = service.relatedSearch(currentPage, memberLimit, keyword);	
		} else {
			memberList = service.selectMemberList(currentPage, memberLimit);			
		}
		
		if(memberList == null) {
			System.out.println("selectMemberList() ��ȸ ����");			
		} else {
			System.out.println("\n[memberList]\n\t" + memberList);
		}
		
		mv.addObject("memberList", memberList);	
		mv.addObject("totalCnt", totalCnt);
		mv.addObject("startPage", startPage);
		mv.addObject("endPage", endPage);
		mv.addObject("currentPage", currentPage);
		mv.addObject("totalPageCnt", totalPageCnt);
		mv.addObject("related_search", keyword);
		
		mv.setViewName("admin/memberManagement");
		return mv;
	}
	
	@RequestMapping(value="/memberWithdraw", method=RequestMethod.POST)
	public ModelAndView updateWithdrawMember(ModelAndView mv
			, RedirectAttributes rttr
			, @RequestParam(value="chk_box", required=false) String [] emails) {
		int result = 0;
		
		if(emails == null) {
			System.out.println("ȸ���� �������ּ���.");
			rttr.addFlashAttribute("msg", "ȸ���� �������ּ���.");
			mv.setViewName("redirect:/admin/");
			return mv;
		}
		
		for(int i=0; i<emails.length; i++) {
			System.out.println("email:\t" + emails[i]);
		}		
		
		result = service.updateWithDrawMember(emails);
		if(result == 0) {
			rttr.addFlashAttribute("msg", "ȸ��Ż�� �����Ͽ����ϴ�.");
			mv.setViewName("redirect:/admin/memberManagement");
			return mv;
		}
		
		rttr.addFlashAttribute("msg", "ȸ���� ������ �����߽��ϴ�.");
		mv.setViewName("redirect:/admin/memberManagement");
		return mv;		
	}
	
	@RequestMapping(value="ask", method= RequestMethod.GET)
	ModelAndView pageAskManagement(ModelAndView mv) {
		mv.setViewName("admin/askManagement");
		return mv;
	}

	@GetMapping("/sales/list")
	public ModelAndView selectSalesLiset(ModelAndView mv) {
		List<Sales> salesList = service.selectSalesLiset();
		mv.addObject("salesList", salesList);
		mv.setViewName("admin/salesList"); // 
		return mv;
	}

	@GetMapping("/sales/read")
    public ModelAndView selectOneSales(ModelAndView mv, HttpServletRequest req
    		, @RequestParam(name = "p_no", defaultValue = "0") String pno) {
		if (pno == "0") {
			mv.setViewName("redirect:salesList");//
			return mv;
		}
		logger.debug(pno);
		logger.debug(req.getParameter("p_no"));
		// DB
		Sales result = service.selectOneSales(pno);
		mv.addObject("sales", result);
		mv.setViewName("admin/sales"); // 
		return mv;
	}
	
	
}
