module GUI
  import 'java.awt'
  import 'java.awt.event'
  import 'javax.swing'
end

class ClickAction
  include ActionListener

  def actionPerformed(event)
    puts "Button got clicked."
  end
end

class Wizard
  def doit
    UIManager.setLookAndFeel UIManager.getSystemLookAndFeelClassName
    
    frame = JFrame.new 'Test Frame'
    frame.setLayout BorderLayout.new
    frame.setSize(200, 200)
    
    frame.add(BorderLayout::CENTER, CustomComponent.new )
    
    pnl = GUI::JPanel.new
    pnl.setBorder( GUI::BorderFactory.createTitledBorder( "Data Carrier Specification" ) )
    pnl.setLayout( GUI::GridLayout.new 3,2 )
    # Project
    pnl.add( GUI::JLabel.new "Project Identifier:" )
    
    frame.add(BorderLayout::NORTH, pnl )
    
    button = JButton.new "Click me!"
    button.addActionListener ClickAction.new
    
    frame.add( BorderLayout::SOUTH, button)
    frame.setLocationRelativeTo nil
    
    frame.defaultCloseOperation = JFrame::EXIT_ON_CLOSE
    frame.visible = true
  end
end