require 'pp'

class String
  def camelize; self.gsub(/(\A|_)(\w)/) {$2.upcase}; end
  def xmlize; self.gsub(/_/, '-'); end
end
class Symbol
  def camelize; to_s.camelize; end
  def xmlize; to_s.xmlize; end
end

class Node

  attr_reader :full_name, :inline, :type, :params, :children, :root
  def initialize(type, parent, extra={}, &blck)
    @type = type = type.to_sym
    @children = {}
    @params = []
    @full_name = type
    @extra = extra
    if parent then
      @full_name = "#{parent.full_name}_#{type}"
      parent.children[type] = self

      @root = parent.root
    end
    root.all_nodes[full_name] = self
    instance_eval(&blck)
  end
  def [](i); @extra[i]; end

  def node(type, extra={}, &blck)
    Node.new(type, self, extra, &blck)
  end

  def required(argname, argtype, extra={})
    @params << {:name => argname, :type => argtype, :required => true}.merge(extra)
  end
  def optional(argname, argtype, extra={})
    @params << {:name => argname, :type => argtype, :required => false}.merge(extra)
  end
  def precode(str=nil); !str and return @precode; @precode = str; end
  def postcode(str=nil); !str and return @postcode; @postcode = str; end
  def code(str); postcode(str); end
  def children_of(nodename)
    @children = @root.all_nodes[nodename].children
  end

  def method_missing(name, *args, &blck)
    clsname = "#{name.camelize}Node"
    if Object.const_defined? clsname then
      type = args.shift
      return Object.const_get(clsname).new(type, self, *args, &blck)
    end

    super
  end

end
class RootNode < Node
  attr_reader :all_nodes
  def initialize(*args)
    @all_nodes = {}
    super
  end
  def root; self; end
end

def root(type, &blck)
  RootNode.new(type, nil, &blck)
end

class Printer

  def initialize()
    @indent = 0
    @output = ""
    @halt_tab = false
  end

  def println(str)
    if str == nil then return self end
    print(str); print("\n"); self
  end

  def <<(str); println(str); end

  def print(str)
    if str == :tab then @output << "  "
    elsif str != nil
      @output << tabs if @output[-1] == "\n"
      @output << str.gsub(/(?<=\n)(?!\Z)/, tabs)
    end; self
  end
  def to_s; @output; end

  protected

  def tabs(); "  " * @indent; end

  def indent(before=nil, after=nil)
    println before
    @indent += 1
    yield
    @indent -= 1
    println after
  end
end


###################################


class RootNode
  attr_reader :imports
  def import(imports); @imports = (@imports || []) + imports; end
  def import_j(imports); import(imports.map{|i| "com.jakemadethis.pinball.#{i}"}); end
  def import_jl(imports); import(imports.map{|i| "com.jakemadethis.pinball.level.#{i}"}); end
end
class EntityNode < Node
  def initialize(type, parent, args, extra={}, &blck)
    extra[:class] ||= type.camelize
    args = [:model] + args
    @postcode = "model.add(new #{extra[:class]}(#{args.join(', ')}));"
    super(type, parent, extra, &blck)
  end
end

class PinballPrinter < Printer

  def initialize(nodes)
    super()

    @imports = %w{game.GameModel LevelException Attachable}.map {|c| "com.jakemadethis.pinball.#{c}"}

    gen_imports(nodes[:level])

    indent "public class PinballNewFactory implements BuilderFactory<Object> {", "}" do
      println "private GameModel model;"
      println "private static float SCALE = 100f;"
      indent "public PinballNewFactory(GameModel model) {", "}" do
        println "this.model = model;"
      end
      indent "public Object create(BuilderNode node) {", "}" do
        println "Attachable rel_to = null;"
        gen_iterate_children([nodes[:level]], "root", "node")
        println "return null;"
      end

      nodes.values.each &method(:gen_node)
    end
  end

  def gen_imports(rootnode)
    println "package com.jakemadethis.pinball.builder;"
    (@imports + rootnode.imports).each do |clss|
      println "import #{clss};"
    end
  end

  def gen_node(node)
    params = (["BuilderNode node", "Attachable rel_to"] + (node[:params] || [])).join(", ")
    indent "private void create_#{node.full_name}(#{params}) {", "}" do
      gen_params(node); println node.precode; gen_children(node); println node.postcode
    end
  end

  def gen_params(node)
    node.params.each &method(:gen_param)
  end

  def gen_param(param)
    getparam = if param[:required]
      %Q{node.attExpected("#{param[:name]}")}
    else
      %Q{node.attOptional("#{param[:name]}", "#{param[:default]}")}
    end

    println case param[:type]
    when :position
      %Q{float[] #{param[:name]} = FactoryUtil.toPosition(#{getparam}, rel_to, SCALE);}
    when :size
      %Q{float[] #{param[:name]} = FactoryUtil.toSize(#{getparam}, SCALE);}
    when :length
      %Q{float #{param[:name]} = FactoryUtil.toLength(#{getparam}, SCALE);}
    when :string
      %Q{String #{param[:name]} = #{getparam};}
    when :integer
      %Q{int #{param[:name]} = FactoryUtil.toInteger(#{getparam});}
    when :float
      %Q{int #{param[:name]} = FactoryUtil.toFloat(#{getparam});}
    end
    
    param[:remap] and 
      gen_remap_param(param[:remap], param[:name], 'float')
  end

  def gen_remap_param(map, name, type)
    map.each.with_index { |s, i|
      println "#{type} #{s} = #{name}[#{i}];"
    }
  end

  def gen_children(node)
    if node.children.empty? then return end

    indent "for(BuilderNode child : node.getChilds()) {", "}" do
      gen_iterate_children(node.children.values, node.type, "child")
    end
  end

  def gen_iterate_children(nodes, parent_name, node_var)
    nodes.each { |c|
      indent %Q{if ("#{c.type.xmlize}".equals(#{node_var}.getNodeName()))} do
        params = param_list(c, node_var, "rel_to").join(", ")
        println %Q{create_#{c.full_name}(#{params});}
      end
      print "else "
    }
    indent "" do
      println %Q{throw new LevelException("Invalid node in #{parent_name}: "+#{node_var}.getNodeName());}
    end
  end

  def param_list(node, *pre)
    pre + (node[:params] || []).map { |p| p.split[1] }
  end
end



root = root(:level) do
  import %w{java.util.ArrayList 
            com.badlogic.gdx.math.Vector2
            com.badlogic.gdx.graphics.Color}
  import_j %w{}
  import_jl %w{Level LightGroup Wall Light Bumper Flipper Frame}

  required "name", :string
  required "size", :size, :remap => [:w, :h]
  precode %Q{rel_to = new Level(name, w, h); model.setSize(w, h);}

  node :ball_start do
    required "at", :position, :remap => [:x, :y]
    code "model.addBallPos(x, y);"
  end

  entity :bumper, [:x, :y, :radius] do
    required "at", :position, :remap => [:x, :y]
    optional "radius", :length, :default => 20
  end

  node :left_flipper do
    required "at", :position, :remap => [:x, :y]
    required "length", :length
    code "model.add(new Flipper(model, x, y, length, Flipper.Type.LEFT));"
  end

  node :right_flipper do
    required "at", :position, :remap => [:x, :y]
    required "length", :length
    code "model.add(new Flipper(model, x, y, length, Flipper.Type.RIGHT));"
  end

  entity :light, [:x, :y, :radius, :color] do
    required "at", :position, :remap => [:x, :y]
    optional "radius", :length, :default => 20
    precode "Color color = Color.RED;"
  end

  entity :light_group, [:vectors, :score] do
    required "score", :integer

    precode %Q{ArrayList<Vector2> vectors = new ArrayList<Vector2>();}
    node :light, :params => ["ArrayList<Vector2> vectors"] do
      required "at", :position, :remap => [:x, :y]
      code %Q{vectors.add(new Vector2(x, y));}
    end

  end

  node :wall do
    precode %Q{Wall.Builder builder = new Wall.Builder(false);}

    node :point, :params => ["Wall.Builder builder"] do
      required "at", :position, :remap => [:x, :y]
      code %Q{builder.addPoint(x, y);}
    end
    node :arc, :params => ["Wall.Builder builder"] do
      required "at", :position, :remap => [:x, :y]
      required "radius", :length
      code %Q{builder.addArc(x, y, radius);}
    end

    postcode %Q{model.add(builder.constructWall(model));}
  end

  node :group do
    precode %Q{rel_to = new Frame(rel_to);}
    children_of :level
  end

  node :frame do
    required "from", :position, :remap => [:x1, :y1]
    required "to", :position, :remap => [:x2, :y2]
    precode %Q{rel_to = new Frame(x1, y1, x2, y2);}
    children_of :level
  end
end

code = %Q{/* 
Generated from:
ruby script/codegen.rb src/com/jakemadethis/pinball/builder/PinballNewFactory.java 
*/
} + PinballPrinter.new(root.all_nodes).to_s
puts code
ARGV[0] and IO.write(ARGV[0], code)