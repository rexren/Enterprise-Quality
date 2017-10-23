package com.hikvision.ga.hephaestus.cert.repository;

import java.util.List;

import com.hikvision.ga.hephaestus.cert.domain.CccPage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;


/**
 * @Description: 3C相关数据查询
 * @author langyicong
 * @date 2017年5月12日
 * @modify modify project structure by langyicong 2017年10月23日
 */
@NoRepositoryBean
public interface CccPageRepository extends JpaRepository<CccPage, Long> {

	/**
	 * 根据文件编号查询3C条目
	 * @param docNo 文件编号
	 * @return 3C列表
	 */
	List<CccPage> findByDocNo(String docNo);

	/**
	 * 关键字搜索3C列表
	 * @param fieldName 查询的领域名称
	 * @param keywords 关键词字符串数组
	 * @return 3C列表
	 */
	List<CccPage> searchCccByKeyword(String fieldName, String[] keywords);
}
