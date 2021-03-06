package org.ccframe.client.module.core.view;

import java.util.List;

import org.ccframe.client.MenuConfig;
import org.ccframe.client.ViewResEnum;
import org.ccframe.client.base.BaseHandler;
import org.ccframe.client.commons.AdminUser;
import org.ccframe.client.commons.ClientManager;
import org.ccframe.client.commons.EventBusUtil;
import org.ccframe.client.commons.ICcModule;
import org.ccframe.client.commons.ICcWindow;
import org.ccframe.client.commons.RestCallback;
import org.ccframe.client.commons.TreeNodeTree;
import org.ccframe.client.commons.TreeUtil;
import org.ccframe.client.commons.ViewUtil;
import org.ccframe.client.commons.WindowEventCallback;
import org.ccframe.client.components.FaBadgeButton;
import org.ccframe.client.components.FaButton;
import org.ccframe.client.components.FaIconType;
import org.ccframe.client.components.FaMenuItem;
import org.ccframe.client.module.core.event.BodyContentEvent;
import org.ccframe.client.module.core.event.LoadWindowEvent;
import org.ccframe.client.service.MainFrameClient;
import org.ccframe.subsys.core.domain.entity.MenuRes;
import org.ccframe.subsys.core.dto.MainFrameResp;
import org.fusesource.restygwt.client.Method;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.RepeatingCommand;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.inject.client.AsyncProvider;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Singleton;
import com.sencha.gxt.core.client.Style.Anchor;
import com.sencha.gxt.core.client.Style.AnchorAlignment;
import com.sencha.gxt.core.client.Style.SelectionMode;
import com.sencha.gxt.data.shared.TreeStore;
import com.sencha.gxt.widget.core.client.Component;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.container.AccordionLayoutContainer;
import com.sencha.gxt.widget.core.client.container.AccordionLayoutContainer.ExpandMode;
import com.sencha.gxt.widget.core.client.container.BorderLayoutContainer;
import com.sencha.gxt.widget.core.client.container.BoxLayoutContainer.BoxLayoutData;
import com.sencha.gxt.widget.core.client.container.HBoxLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VBoxLayoutContainer;
import com.sencha.gxt.widget.core.client.event.CollapseEvent;
import com.sencha.gxt.widget.core.client.event.CollapseEvent.CollapseHandler;
import com.sencha.gxt.widget.core.client.menu.Item;
import com.sencha.gxt.widget.core.client.menu.Menu;
import com.sencha.gxt.widget.core.client.tree.Tree;

@Singleton
public class MainFrame implements IsWidget{

	private MainFrameClient mainFrameClient = GWT.create(MainFrameClient.class);

	private static MainFrameUiBinder uiBinder = GWT.create(MainFrameUiBinder.class);

	interface MainFrameUiBinder extends UiBinder<Component, MainFrame> {}

	@UiField
	public ContentPanel menuPanel;
	
	@UiField
	public HBoxLayoutContainer hbox;
	
	@UiField
	public BorderLayoutContainer borderLayoutContainer;

	@UiField
	public Button userButton;

	@UiField
	public FaBadgeButton navButton1;
	
	@UiField
	public VBoxLayoutContainer buttonBox;
	
	@UiField
	public ContentPanel centerPanel;
	
	public static AdminUser adminUser;

	@UiField
	FaButton fastMenu1;
	@UiField
	FaButton fastMenu2;
	@UiField
	FaButton fastMenu3;
	@UiField
	FaButton fastMenu4;
	
	@UiHandler("navButton1")
	void handleClick(ClickEvent e) {
//		Window.alert("test");
//		navButton1.setBadgeText("889");
//		navButton1.setFaIconType(FaIconType.PLUG);
	}

	/**
	 * 获取全局的登录用户，用于机构等判断。但是为了安全服务器侧还是要做多一个判断。
	 * @return
	 */
	public AdminUser getAdminUser(){
		return adminUser;
	}

	private Menu menu;
	private Menu createMenu() {
		Menu menu = new Menu();
		FaMenuItem menuItem = new FaMenuItem("更改密码");
		menuItem.setFaIconType(FaIconType.KEY);
		menuItem.addSelectionHandler(new SelectionHandler<Item>(){
			@Override
			public void onSelection(SelectionEvent<Item> event) {
				EventBusUtil.fireEvent(new LoadWindowEvent<Integer, Void, EventHandler>(ViewResEnum.USER_PASSWORD_WINDOW, null, new WindowEventCallback<Void>(){
					@Override
					public void onClose(Void returnData) {}
				}));
			}
		});
		menu.add(menuItem);
		
//		menuItem = new FaMenuItem("这份Windows合法吗？");
//		menuItem.setFaIconType(FaIconType.QUESTION);
//		menu.add(menuItem);

		menuItem = new FaMenuItem("注销");
		menuItem.setFaIconType(FaIconType.SIGN_OUT);
		menuItem.addSelectionHandler(new SelectionHandler<Item>(){
			@Override
			public void onSelection(SelectionEvent<Item> event) {
				ClientManager.getMainFrameClient().doLogout(new RestCallback<String>(){
					@Override
					public void onSuccess(Method method, String response) {
						this.redirect(GWT.getHostPageBaseURL() + response + Window.Location.getQueryString());
					}
				});
			}
		});
		menu.add(menuItem);
		
		//关于本系统
		menuItem = new FaMenuItem("关于本系统");
		menuItem.setFaIconType(FaIconType.COFFEE);
		menuItem.addSelectionHandler(new SelectionHandler<Item>(){
			@Override
			public void onSelection(SelectionEvent<Item> event) {
				EventBusUtil.fireEvent(new LoadWindowEvent<Integer, Void, EventHandler>(ViewResEnum.ABOUT_SYSTEM, null, new WindowEventCallback<Void>(){
					@Override
					public void onClose(Void returnData) {}
				}));
			}
		});
		menu.add(menuItem);

		return menu;
	}
	 
	@UiHandler("userButton")
	void handleUserButtonClick(ClickEvent e) {
		if(menu == null){
			menu = createMenu();
			menu.addStyleName("cc-menu");
		}
		menu.show(userButton.getElement(), new AnchorAlignment(Anchor.TOP_RIGHT, Anchor.BOTTOM_RIGHT, true));
	}

	private Widget widget;

	public Widget asWidget() {
		if (widget == null) {
			widget = uiBinder.createAndBindUi(this);
		}

		menuPanel.getHeader().getElement().getParentElement().addClassName("frameMainColor leftMenuTop");

		mainFrameClient.getMainFrameDto(new RestCallback<MainFrameResp>(){
			@Override
			public void onSuccess(Method method, MainFrameResp mainFrameDto) {
				BoxLayoutData vBoxLayoutData = new BoxLayoutData();
				vBoxLayoutData.setFlex(1.0);
				
				AccordionLayoutContainer myAccord = new AccordionLayoutContainer();
				myAccord.addStyleName("cc-lmenu");
				
				myAccord.setExpandMode(ExpandMode.SINGLE_FILL);
				List<TreeNodeTree> treeNodeTreeList = mainFrameDto.getTreeNodeTree().getSubNodeTree();
				for(TreeNodeTree treeNodeTree: treeNodeTreeList){
					TreeStore<TreeNodeTree> menuTreeStore = new TreeStore<TreeNodeTree>(TreeUtil.treeNodeTreeKeyProvider);
					TreeUtil.loadTreeStore(menuTreeStore, treeNodeTree, false);

					//申明一个节点为String的树
					final Tree<TreeNodeTree, String> tree = new Tree<TreeNodeTree, String>(menuTreeStore, TreeUtil.treeValueProvider);
					tree.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
					tree.getSelectionModel().addSelectionHandler(new SelectionHandler<TreeNodeTree>(){
						@Override
						public void onSelection(SelectionEvent<TreeNodeTree> selection) {
							mainFrameClient.getMenuRes(selection.getSelectedItem().getSysObjectId(), new RestCallback<MenuRes>(){

								@Override
								public void onSuccess(Method method, MenuRes response) {
									if(response.getResUrl() != null){
										EventBusUtil.fireEvent(new BodyContentEvent(ViewResEnum.fromValue(response.getResUrl())));
									}
								}
							});
						}
						
					});
					tree.setAutoExpand(true);
					
					ContentPanel menuCategory = new ContentPanel();
					menuCategory.setCollapsible(false);
					menuCategory.setAnimCollapse(true);
					menuCategory.addStyleName("nav-menu-accordion");
					menuCategory.setHeadingText(treeNodeTree.getTreeNodeNm());
					menuCategory.add(tree);
					menuCategory.addCollapseHandler(new CollapseHandler(){
						@Override
						public void onCollapse(CollapseEvent event) {
							tree.getSelectionModel().deselectAll();
						}
					});
					
					myAccord.add(menuCategory.asWidget());
				}
				buttonBox.add(myAccord, vBoxLayoutData);
				myAccord.setActiveWidget(myAccord.iterator().next());
				
				if(mainFrameDto.getFastMenuRes().isEmpty()){
					fastMenu1.setVisible(false);
					fastMenu2.setVisible(false);
					fastMenu3.setVisible(false);
					fastMenu4.setVisible(false);
				}else{
					FaButton[] fastMenus = new FaButton[]{fastMenu1,fastMenu2,fastMenu3,fastMenu4};
					for(int i = 0; i < 4; i ++){
						final MenuRes menuRes = mainFrameDto.getFastMenuRes().get(i);
						fastMenus[i].setTitle(menuRes.getResNm());
						fastMenus[i].setFaIconType(FaIconType.fromCode(menuRes.getIcon()));
						fastMenus[i].addClickHandler(new ClickHandler(){
							@Override
							public void onClick(ClickEvent event) {
								EventBusUtil.fireEvent(new BodyContentEvent(ViewResEnum.fromValue(menuRes.getResUrl())));
							}
						});
					}
				}

				adminUser = mainFrameDto.getAdminUser();

				//todo 用户头像
				userButton.setHTML("<img src='"+GWT.getHostPageBaseURL()+ (GWT.getHostPageBaseURL().endsWith("/") ? "" : "/")+"ccframe/images/testlogo.jpg'/>" + mainFrameDto.getAdminUser().getUserNm() + "<li class='fa fa-sort-desc'/>");
				borderLayoutContainer.forceLayout();
			}
		});

		initEvent();
		return widget;
	}

	@SuppressWarnings("unchecked")
	public void initEvent(){
		EventBusUtil.addHandler(BodyContentEvent.TYPE, new BaseHandler<BodyContentEvent>(){
			@Override
			public void action(final BodyContentEvent event) {
				Scheduler.get().scheduleFixedDelay(new RepeatingCommand(){
					@Override
					public boolean execute() {
						centerPanel.clear();
						AsyncProvider<? extends IsWidget> provider = MenuConfig.getProvider(event.getViewResEnum());
						if(provider != null){
							provider.get(new AsyncCallback<IsWidget>(){
								@Override
								public void onFailure(Throwable caught) {
									ViewUtil.error("错误", "模块加载失败");
								}
								@Override
								public void onSuccess(IsWidget result) {
									mainFrameClient.menuHit(event.getViewResEnum().toValue(), new RestCallback<Void>(){
										@Override
										public void onSuccess(Method method, Void response) {
										}
									});
									if(result instanceof ICcModule){
										centerPanel.add(result.asWidget());
										centerPanel.forceLayout();
										((ICcModule)result).onModuleReload(event);
									}
								}
							});
						}
						return false;
					}
				}, 100);
			}
		});
		EventBusUtil.addHandler(LoadWindowEvent.TYPE, new BaseHandler<LoadWindowEvent>(){
			@Override
			public void action(final LoadWindowEvent event) {
				AsyncProvider<? extends IsWidget> provider = MenuConfig.getProvider(event.getViewResEnum());
				if(provider != null){
					provider.get(new AsyncCallback<IsWidget>(){
						@Override
						public void onFailure(Throwable caught) {
							ViewUtil.error("错误", "模块加载失败");
						}
						@Override
						public void onSuccess(IsWidget result) {
							if(result instanceof ICcWindow){
								result.asWidget();
								((ICcWindow)result).show(event);
							}
						}
					});
				}
			}
		});
	}
}
