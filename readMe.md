��װһ��״̬�� 
--------------

ʹ��: 
		final BaseStatesMachine machine = new BaseStatesMachine("test");
		List<BaseStatesMachine.BaseState> list = new ArrayList<>();
		list.add(machine.new BaseState(STATE_1));
		list.add(machine.new BaseState(STATE_2));
		list.add(machine.new BaseState(STATE_3));
		list.add(machine.new BaseState(STATE_4));
		machine.setStatesData(list);
		machine.setUIUpdateInter(new UIUpdateInter() {
		@Override
		public void updateUIByStatus(final int states) {
			Log.e("soar", "get  cureent state === " + machine.getLocalCurrentState());
		 runOnUiThread(new Runnable() {
			 @Override
			 public void run() {
				switch (states) {
				case STATE_1:
					state.setText("state ---- " + STATE_1);
					break;
				case STATE_2:
					state.setText("state ---- " + STATE_2);
					break;
				case STATE_3:
					state.setText("state ---- " + STATE_3);
					break;
				case STATE_4:
					state.setText("state ---- " + STATE_4);
					break;
				}


			}
			});

		}
	 });
        machine.start();

��Ŀ�л����� xUtils , ����ѧϰʹ����