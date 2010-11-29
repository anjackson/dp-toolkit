A simple UI that allows FITS (and perhaps other profiling tools) to be run on content files.

Note that full identification would be a good fit for a Ruby DSL. 
Stack conditions for fast-fail, allow .unzip blocks to peel apart things.

if file.match(regex) do
	file.unpack each do { item
	  if( file.name == Content_Type )
	end
end

May have to be JRuby as it seems the Ruby ZIP lib is not great.
