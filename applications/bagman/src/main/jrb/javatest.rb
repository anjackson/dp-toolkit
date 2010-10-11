require 'java'

java_import 'java.awt.event.ActionListener'
java_import 'javax.swing.JButton'
java_import 'javax.swing.JFrame'
java_import 'javax.swing.UIManager'
java_import 'javax.swing.JComponent'
java_import 'java.awt.BorderLayout'
java_import 'java.awt.GradientPaint'
java_import 'java.awt.Color'

module GUI
  import 'java.awt'
  import 'java.awt.event'
  import 'javax.swing'
end

class CustomComponent < JComponent
 
    def initialize
      @paint = GradientPaint.new(0, 0, Color::BLACK, 0, 20, Color::WHITE)
    end
 
    def paintComponent(g)
        g.setPaint(@paint)
        g.fillRect(0, 0, getWidth, getHeight)
    end
end

class ClickAction
  include ActionListener

  def actionPerformed(event)
    puts "Button got clicked."
  end
end

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

if __FILE__ == $0
  # TODO Generated stub
end
