#import "Common/ShaderLib/Texture.glsllib"
 
varying vec2 texCoord;
 
uniform sampler2D m_ColorMap;
 
uniform float m_AlphaMultiplier;
uniform vec4 m_HueColor;
uniform float m_HueMultiplier;
uniform float m_TexturePortionX;
uniform float m_TexturePortionY;
 
void main(){
    gl_FragColor = Texture_GetColor(m_ColorMap, vec2(texCoord.x * m_TexturePortionX, texCoord.y * m_TexturePortionY));
    #ifdef USE_ALPHA_MULTIPLIER
        gl_FragColor.a *= m_AlphaMultiplier;
    #endif
    #ifdef USE_HUE_COLOR
    	gl_FragColor.r *= m_HueColor.r * m_HueMultiplier;
    	gl_FragColor.g *= m_HueColor.g * m_HueMultiplier;
    	gl_FragColor.b *= m_HueColor.b * m_HueMultiplier;
    	gl_FragColor.a *= m_HueColor.a * m_HueMultiplier;
    #endif
}