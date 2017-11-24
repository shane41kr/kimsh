package com.bookshop01.goods.dao;

import java.util.ArrayList;
import java.util.HashMap;

import com.bookshop01.goods.vo.GoodsBean;

public interface GoodsDao {
	public ArrayList listGoods(String goodsType ) throws Exception;
	public GoodsBean goodsDetail(String goods_id) throws Exception;
	public ArrayList goodsDetailImage(String goods_id) throws Exception;
	public ArrayList keywordSearch(String keyword) throws Exception;
	public ArrayList searchGoods(String searchWord) throws Exception;
	public ArrayList searchGoods(HashMap searchMap) throws Exception;
	public ArrayList recoGoodsList(String goods_id) throws Exception;
	public ArrayList recoGoodsPoint(String goods_id) throws Exception;
	public ArrayList myRecoList(ArrayList my_reco_list)throws Exception;
	public void addShopingReco(ArrayList reco_shoping_list, String recoed_goods_id, String member_id) throws Exception;
	
}
