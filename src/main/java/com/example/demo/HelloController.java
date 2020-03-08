package com.example.demo;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {
	
	@Autowired
	private JdbcTemplate jdbc; 

	@RequestMapping("/")
	public String main() {
		
		try {
			this.jdbc.execute("DROP TABLE IF EXISTS test_table");
			this.jdbc.execute("CREATE TABLE test_table (\n" + 
					"`id` int(11) unsigned NOT NULL AUTO_INCREMENT,\n" + 
					" `value` varchar(255),\n" + 
					"  PRIMARY KEY (`id`)\n" + 
					") ENGINE=InnoDB DEFAULT CHARSET=utf8;\n" + 
					""); 			
		}
		catch(Exception e)
		{
		}
		
		return "Database Ready";
	}
	
	@RequestMapping("/add")
	public double add() {
		
		double randomNum = Math.random();
		
		this.jdbc.update("INSERT INTO test_table(value) VALUES('" + randomNum + "')"); 
		
		return randomNum;		
	}
	
	@RequestMapping("/show")
	public List<Map<String, Object>> Show() {
		
		List<Map<String, Object>> list = this.jdbc.queryForList("SELECT * FROM test_table");
		
		return list;	
	}
}
