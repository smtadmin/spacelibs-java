package com.siliconmtn.io.api.validation.factory;

// JDK 11.x
import java.util.Map;

// Spring 5.3.x
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpStatus;

// Spacelibs
import com.siliconmtn.data.text.StringUtil;
import com.siliconmtn.io.api.EndpointRequestException;
import com.siliconmtn.io.api.validation.factory.AbstractParser.AttributeKey;

/****************************************************************************
 * <b>Title</b>: ParserFactory.java
 * <b>Project</b>: spacelibs-java
 * <b>Description: </b> Gives an instance of appropriate parser/packager
 * for a specific a aspect/controller call
 * <b>Copyright:</b> Copyright (c) 2021
 * <b>Company:</b> Silicon Mountain Technologies
 * 
 * @author Bala Gayatri Bugatha
 * @version 3.0
 * @since Mar 10, 2021
 * @updates:
 ****************************************************************************/
@Configuration
@PropertySource("classpath:application.properties")
public class ParserFactory {
	
	/**
	 * Calls back to the application.properties file of the app to get
	 * the list of classname.methodname/parserclassname pairs.
	 */
	@Value("#{${parserMapper}}") 
	private Map<String,String> builderMapper;
	
	@Autowired
	protected AutowireCapableBeanFactory autowireCapableBeanFactory;
	
	@Autowired
    private ApplicationContext applicationContext;
	
	/**
	 * Checks the parserMapper property in the application's config file for the parser associated with
	 * the passed classname.methodname key.
	 * @param controllerName the classname.methodname combo key for the parser we are looking for
	 * @param attributes map of attributes from endpoint other than the body
	 * @return ParserIntfc that will be used to parse the request body into ValidationDTOs
	 * @throws EndpointRequestException When unable to create an instance of the controller name
	 */
	public ParserIntfc parserDispatcher(String beanName, Map<AttributeKey, Object> attributes)
            throws EndpointRequestException {
    	String parserClassName = builderMapper.get(beanName);
        if (StringUtil.isEmpty(parserClassName))
            return null;

        try {
            ParserIntfc parser = (ParserIntfc) applicationContext.getBean(parserClassName);
            parser.setAttributes(attributes);
            autowireCapableBeanFactory.autowireBean(parser);
            return parser;
        } catch (Exception e) {
            throw new EndpointRequestException("Failed to create data parser", e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
