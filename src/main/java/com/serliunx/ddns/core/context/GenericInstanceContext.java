package com.serliunx.ddns.core.context;

/**
 * 简易的容器实现, 需要手动进行刷新、添加实例工厂.
 * @author SerLiunx
 * @since 1.0
 */
public class GenericInstanceContext extends AbstractInstanceContext {

	private final boolean clearable;

	public GenericInstanceContext(boolean clearable) {
		this.clearable = clearable;
	}

	public GenericInstanceContext() {
		this(false);
	}

	@Override
	protected void clear0() {
		clearCache();
	}

	@Override
	public boolean isClearable() {
		return clearable;
	}
}
