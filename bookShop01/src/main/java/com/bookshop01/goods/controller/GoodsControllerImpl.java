package com.bookshop01.goods.controller;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.bookshop01.common.controller.BaseController;
import com.bookshop01.goods.service.GoodsService;
import com.bookshop01.goods.vo.GoodsBean;

@Controller("goodsController")
@RequestMapping(value="/goods")
public class GoodsControllerImpl  extends BaseController implements GoodsController {
	@Autowired
	GoodsService goodsService;
	
	@RequestMapping(value="/goodsDetail.do" ,method = RequestMethod.GET)
	public ModelAndView goodsDetail(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String goods_id = request.getParameter("goods_id");
		String fileName=getFileName(request);
		HttpSession session=request.getSession();
		HashMap goodsMap=goodsService.goodsDetail(goods_id);
		ModelAndView mav = new ModelAndView(fileName);
		mav.addObject("goodsMap", goodsMap);
		GoodsBean goodsBean=(GoodsBean)goodsMap.get("goods");
		add_goods_in_sticky(goods_id,goodsBean,session);
		return mav;
	}
	
	@RequestMapping(value="/keywordSearch.do" ,method = RequestMethod.GET)
	public void keywordSearch(HttpServletRequest request, HttpServletResponse response) throws Exception{
		//request.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=utf-8");
		String keyword = request.getParameter("keyword");
		 System.out.println(keyword);
		if(keyword == null || keyword.equals(""))
		   return ;
	
		keyword = keyword.toUpperCase();
	    List keywordList =goodsService.keywordSearch(keyword);
	    
	    
			 // ���þ� ���� ���
	   PrintWriter out=response.getWriter();
	   out.print(keywordList.size());
	   out.print("|");
	   // ���þ� ����� CSV ������� ���
	   for(int i=0; i<keywordList.size(); i++){
		  String key = (String)keywordList.get(i);
		  out.print(key);
		  if(i<keywordList.size()-1) {
			   out.print(",");
		  }
	  }
		return ;
	}
	
	@RequestMapping(value="/searchGoods.do" ,method = RequestMethod.GET)
	public ModelAndView searchGoods(HttpServletRequest request, HttpServletResponse response) throws Exception{
		String searchWord = request.getParameter("searchWord");
		String search=request.getParameter("search");
		System.out.println(searchWord);
		System.out.println(search);
		HashMap searchMap=new HashMap();
		searchMap.put("searchWord", searchWord);
		searchMap.put("search", search);
		String fileName=getFileName(request);
		ArrayList search_goods_list=goodsService.searchGoods(searchMap);
		ModelAndView mav = new ModelAndView(fileName);
		mav.addObject("search_goods_list", search_goods_list);
		return mav;
		
	}
	
	/*@RequestMapping(value="/searchGoods.do" ,method = RequestMethod.GET)
	public ModelAndView searchGoods(HttpServletRequest request, HttpServletResponse response) throws Exception{
		String searchWord = request.getParameter("searchWord");
		String fileName=getFileName(request);
		ArrayList search_goods_list=goodsService.searchGoods(searchWord);
		ModelAndView mav = new ModelAndView(fileName);
		mav.addObject("search_goods_list", search_goods_list);
		return mav;
		
	}*/
	
	private void add_goods_in_sticky(String goods_id,GoodsBean goodsBean,HttpSession session){
		boolean already_existed=false;
		ArrayList<GoodsBean> sticky_goods_list; //�ֱ� �� ��ǰ ���� ArrayList
		sticky_goods_list=(ArrayList<GoodsBean>)session.getAttribute("sticky_goods_list");
		
		if(sticky_goods_list!=null){
			if(sticky_goods_list.size()<4){ //�̸��� ��ǰ ����Ʈ�� ��ǰ������ ���� ������ ���
				for(int i=0; i<sticky_goods_list.size();i++){
					GoodsBean _goodsBean=(GoodsBean)sticky_goods_list.get(i);
					if(goods_id.equals(_goodsBean.getGoods_id())){
						already_existed=true;
						break;
					}
				}
				if(already_existed==false){
					sticky_goods_list.add(goodsBean);
				}
			}
			
		}else{
			sticky_goods_list =new ArrayList<GoodsBean>();
			sticky_goods_list.add(goodsBean);
			
		}
		session.setAttribute("sticky_goods_list",sticky_goods_list);
		session.setAttribute("sticky_list_num", sticky_goods_list.size());
	}
}
