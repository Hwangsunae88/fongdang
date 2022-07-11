package kh.spring.fongdang.admin.model.dao;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import kh.spring.fongdang.admin.domain.Sales;

@Repository
public class AdminDao {
	@Autowired
	private SqlSession sqlSession;
	
	
	
	/* �ݵ����� List ��ȸ  */
	public List<Sales> selectSalesLiset() {
		return sqlSession.selectList("Sales.selectSalesLiset");
	}
	
	/* �ݵ����� �� N�� ��ȸ  */
	public Sales selectOneSales(String pno) {
		return sqlSession.selectOne("Sales.selectOneSales");
	}
}
