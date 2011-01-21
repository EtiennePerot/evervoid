#import "Common/ShaderLib/Texture.glsllib"
 
varying vec2 texCoord;
 
uniform sampler2D m_AlphaMap;
 
uniform float m_AlphaMultiplier;
uniform vec4 m_ShadeColor;
uniform float m_ShadeAngle;
uniform float m_ShadePortion;
uniform float m_ShadeGradientPortion;
 
void main(){
    gl_FragColor = vec4(m_ShadeColor.r, m_ShadeColor.g, m_ShadeColor.b, m_ShadeColor.a * Texture_GetColor(m_AlphaMap, texCoord).a);
    float shade = texCoord.x;
    // Todo: Compute projection of point on rotated line according to m_ShadeAngle
    if(shade > m_ShadePortion){
    	gl_FragColor = vec4(0, 0, 0, 0);
    }
    else{
    	if(shade > m_ShadePortion - m_ShadeGradientPortion){
    		gl_FragColor = vec4(gl_FragColor.r, gl_FragColor.g, gl_FragColor.b, gl_FragColor.a * (1.0 - (shade - m_ShadePortion + m_ShadeGradientPortion) / m_ShadeGradientPortion));
    	}
		#ifdef USE_ALPHA_MULTIPLIER
			gl_FragColor = vec4(gl_FragColor.r, gl_FragColor.g, gl_FragColor.b, gl_FragColor.a * m_AlphaMultiplier);
		#endif
	}
}