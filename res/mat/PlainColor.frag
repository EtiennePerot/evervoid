#import "Common/ShaderLib/Texture.glsllib"
 
varying vec2 texCoord;
 
uniform vec4 m_Color;
 
uniform float m_AlphaMultiplier;
 
void main(){
	gl_FragColor = m_Color.r;
    #ifdef USE_ALPHA_MULTIPLIER
        gl_FragColor.a *= m_AlphaMultiplier;
    #endif
}