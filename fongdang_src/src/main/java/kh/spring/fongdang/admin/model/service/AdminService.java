package kh.spring.fongdang.admin.model.service;

import java.util.List;

import kh.spring.fongdang.admin.domain.Sales;


public interface AdminService {

	
	/* �ݵ����� List ��ȸ  */
	public List<Sales> selectSalesLiset();
	
	/* �ݵ����� �� N�� ��ȸ  */
	public Sales selectOneSales(String pno);
}
