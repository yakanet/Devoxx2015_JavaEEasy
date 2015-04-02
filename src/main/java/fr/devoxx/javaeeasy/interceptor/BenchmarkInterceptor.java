package fr.devoxx.javaeeasy.interceptor;

import java.util.logging.Logger;

import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;

@Benchmark
@Interceptor
public class BenchmarkInterceptor {

	@Inject
	private Logger logger; 
	
	@AroundInvoke
	public Object check(final InvocationContext ic) throws Exception {
		long startTime = System.currentTimeMillis();
		Object result = ic.proceed();
		long endTime = System.currentTimeMillis();
		logger.info(String.format("Time spent on %s : %d ms", ic.getMethod(),  endTime - startTime));
		return result;
	}
}
